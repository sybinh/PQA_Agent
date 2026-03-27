from pathlib import Path
from typing import Dict, Optional, Any
from requests import Session
from parse import ResourceShapeParser
from spec import PREFIXES, Reference, to_python_type_str, Occurrence
from misc import sanitize_cammel_case, sanitize_snake_case
from jinja2 import Environment
import typing


RQ1_SERVER_PRODUCTIVE = (
    "https://rb-dgsrq1-oslc-p.de.bosch.com/cqweb/oslc/repo/RQ1_PRODUCTIVE/db/RQONE/"
)
RQ1_SERVER_APPROVAL = (
    "https://rb-dgsrq1-approval.de.bosch.com/cqweb/oslc/repo/RQ1_APPROVAL/db/RQONE/"
)
RQ1_SERVER_ACCEPTANCE = (
    "https://rb-dgsrq1-oslc-q.de.bosch.com/cqweb/oslc/repo/RQ1_ACCEPTANCE/db/RQONE/"
)

RESOURCE_TEMPLATE = """
from .base import Resource, QueryStatement, BaseRecord
from pydantic import ConfigDict, Field, field_validator
import typing
from datetime import datetime, timezone
from enum import Enum
import json

{% if imports %}
from typing import TYPE_CHECKING

if TYPE_CHECKING:
{% for im in imports %}
    {{im}}
{% endfor %}
{% endif %}

class {{class_name}}(BaseRecord):
    {% if description %}
    '''
    {{description}}
    '''
    {% endif %}
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    {% for p in properties|sort(attribute="name") -%}
    {% if p.document %}#: {{p.document}}{% endif %}
    {{p.name}}: {{p.type}} = Field(alias="{{p.alias}}"{% if p.default %}, default={{p.default}}{% endif %}{% if p.default_factory %}, default_factory={{p.default_factory}}{% endif %})
    {% endfor %}

    @classmethod
    def shape_id(cls) -> str:
        return "{{shape_id}}"

    @classmethod
    def shape_name(cls) -> str:
        return "{{shape_name}}"

    @field_validator('*', mode="before")
    @classmethod
    def empty_str_to_none(cls, v):
        if v == '':
            return None
        return v

    {% if unwrap_fields|length > 0 %}
    @field_validator({% for field in unwrap_fields %}'{{field}}', {% endfor %}mode="before")
    @classmethod
    def list_to_resource(cls, v):
        if isinstance(v, list):
            return v[0]
        return v
    {% endif %}


class {{class_name}}Property(Enum):
    {% for p in properties|sort(attribute="name") -%}
    {{p.name}} = "{{p.alias}}"
    {% endfor %}

    def reformat(self, value) -> str:
            if isinstance(value, Resource):
                return f"<{value.uri}>"
            if isinstance(value, datetime):
                dt = value.astimezone(timezone.utc)
                return f'"{dt.isoformat().replace("+00:00", "Z")}"'
            return json.dumps(value)

    def is_one_of(self, values: typing.List[typing.Any]) -> QueryStatement:
        return QueryStatement("{} in [{}]".format(self.value,",".join(map(lambda x: self.reformat(x), values))))

    def nested(self, value: typing.Union[QueryStatement, list[typing.Union[str, Enum]]]) -> QueryStatement:
        if isinstance(value, QueryStatement):
            return QueryStatement(self.value + {{'"{"'}} + str(value) + {{'"}"'}})
        else:
            return QueryStatement(self.value + {{'"{"'}} + ",".join([str(x) for x in value]) + {{'"}"'}})

    def __eq__(self, other):
            return QueryStatement(f"{self.value}={self.reformat(other)}")

    def __ne__(self, other):
        return QueryStatement(f"{self.value}!={self.reformat(other)}")

    def __lt__(self, other):
        return QueryStatement(f"{self.value}<{self.reformat(other)}")

    def __le__(self, other):
        return QueryStatement(f"{self.value}<={self.reformat(other)}")

    def __gt__(self, other):
        return QueryStatement(f"{self.value}>{self.reformat(other)}")

    def __ge__(self, other):
        return QueryStatement(f"{self.value}>={self.reformat(other)}")

    def __str__(self):
        return self.value

    def __repr__(self):
        return self.value


def create_{{class_name|lower}}_changeset(
    {% for p in properties|sort(attribute="name") if p.name != "uri" and p.alias != "dcterms:type" -%}
    {{p.name}}: {% if p.type.startswith("typing.Optional") %}{{p.type}}{% else %}typing.Optional[{{p.type}}]{% endif %} = None,
    {% endfor %}
) -> {{class_name}}:
    '''
    Create a {{class_name}} changeset.
    '''
    fields = {}
    {% for p in properties|sort(attribute="name") if p.name != "uri" and p.alias != "dcterms:type" -%}
    if {{p.name}} is not None:
        fields["{{p.alias}}"] = {{p.name}} 
    {% endfor %}

    fields["dcterms:type"] = "{{record_type}}"
    return {{class_name}}(**fields)
"""


def literal_value_str(value: Any) -> str:
    if isinstance(value, str):
        return f'"{value}"'
    return str(value)


class ResourceGenerator(object):
    def __init__(
        self,
        session: Session,
        shape_id: str,
        support_search_terms: bool = False,
        name: Optional[str] = None,
        base_oslc_url: str = RQ1_SERVER_PRODUCTIVE,
        prefixes: Dict[str, str] = PREFIXES,
    ):
        self.shape_id = shape_id
        self.search_terms = support_search_terms
        self.name = name
        self.session = session
        self.session.headers.update({"Accept": "application/rdf+xml"})
        self.base_url = base_oslc_url
        self.prefixes = prefixes
        self.parse_shape()

    def parse_shape(self):
        shape_url = self.base_url + f"shape/{self.shape_id}"
        parser = ResourceShapeParser(shape_url, session=self.session)
        self.shape = parser.parse()

    def class_name(self) -> str:
        if self.name is not None:
            return sanitize_cammel_case(self.name)
        else:
            assert self.shape.title is not None
            return sanitize_cammel_case(self.shape.title)

    def file_name(self) -> str:
        if self.name is not None:
            return sanitize_snake_case(self.name)
        else:
            assert self.shape.title is not None
            return sanitize_snake_case(self.shape.title)

    def get_alias(self, prop_def: str) -> str:
        for k, v in self.prefixes.items():
            if prop_def.startswith(v):
                return prop_def.replace(v, f"{k}:")
        raise ValueError(f"Cannot get alias of `{prop_def}`")

    def generate_resource(self) -> str:
        model = {}
        model["class_name"] = self.class_name()
        model["description"] = self.shape.description
        model["shape_id"] = self.shape_id
        model["shape_name"] = self.name
        model["record_type"] = self.name if self.name is not None else self.shape.title
        model["imports"] = set()
        properties = []

        for p in self.shape.properties:
            prop_model = {}
            prop_model["name"] = sanitize_snake_case(p.name)
            prop_model["alias"] = self.get_alias(p.propertyDefinition.ref)

            # resolve type
            if p.allowedValues is not None and len(p.allowedValues) > 0:
                if isinstance(p.allowedValues[0], Reference):
                    prop_model["type"] = "Resource"
                else:
                    prop_model["type"] = (
                        f"typing.Literal[{', '.join([literal_value_str(x) for x in p.allowedValues])}]"
                    )
            elif len(p.valueType) == 0:
                prop_model["type"] = "typing.Any"
            elif len(set(p.valueType)) == 1:
                type_name = to_python_type_str(p.valueType[0].ref)
                if (
                    (type_name == "Resource")
                    and (p.valueShape is not None)
                    and (
                        (generator := get_generator_from_reference(p.valueShape))
                        is not None
                    )
                ):
                    prop_model["type"] = "typing.Union[Resource, '{}']".format(
                        generator.class_name()
                    )
                    if self.class_name != generator.class_name:
                        model["imports"].add(
                            "from .{} import {}".format(
                                generator.file_name(), generator.class_name()
                            )
                        )
                else:
                    prop_model["type"] = type_name
            else:
                type_names = set([to_python_type_str(x.ref) for x in set(p.valueType)])
                if (
                    ("Resource" in type_names)
                    and (p.valueShape is not None)
                    and (
                        (generator := get_generator_from_reference(p.valueShape))
                        is not None
                    )
                ):
                    type_names.add("'{}'".format(generator.class_name()))
                    model["imports"].add(
                        "from .{} import {}".format(
                            generator.file_name(), generator.class_name()
                        )
                    )
                prop_model["type"] = "typing.Union[" + ", ".join(type_names) + "]"
            if p.occurs in [Occurrence.ZERO_OR_MANY, Occurrence.ONE_OR_MANY]:
                prop_model["type"] = "typing.List[" + prop_model["type"] + "]"
                prop_model["default_factory"] = "lambda: []"
            else:
                if (
                    p.occurs == Occurrence.EXACTLY_ONE
                    and prop_model["type"] == "Resource"
                ):
                    prop_model["unwrap"] = True
                prop_model["type"] = "typing.Optional[" + prop_model["type"] + "]"
                prop_model["default"] = "None"

            if (
                p.title is not None
                and p.description is not None
                and p.description.strip() != ""
            ):
                prop_model["document"] = f"**{p.title}**: {p.description}"
            elif p.title is not None:
                prop_model["document"] = f"**{p.title}**"
            elif p.description is not None and p.description.strip() != "":
                prop_model["document"] = f"{p.description}"
            properties.append(prop_model)

        # resolve name conflict
        existing_names = set()
        duplicates = set()
        for tmp in properties:
            if tmp["name"] in existing_names:
                duplicates.add(tmp["name"])
            else:
                existing_names.add(tmp["name"])
        for tmp in properties:
            if tmp["name"] in duplicates:
                tmp["name"] = tmp["alias"].replace(":", "__")

        # collect unwrap fields
        unwrap_fields = []
        for tmp in properties:
            if tmp.get("unwrap"):
                unwrap_fields.append(tmp["name"])
        model["unwrap_fields"] = unwrap_fields

        model["properties"] = properties
        env = Environment()
        template = env.from_string(RESOURCE_TEMPLATE)
        return template.render(model)

    def generate_resource_file(self, dir: Path):
        dir.mkdir(parents=True, exist_ok=True)
        dir.joinpath(self.file_name() + ".py").write_text(self.generate_resource())


GENERATORS: dict[str, ResourceGenerator] = {}


def set_generators(generators: dict[str, ResourceGenerator]):
    global GENERATORS
    GENERATORS = generators


def get_generator_from_reference(ref: typing.Union[Reference, None]):
    if ref is None:
        return None
    shape_id = Path(ref.ref).name
    return GENERATORS.get(shape_id)
