from typing import Any, List, Optional
from dataclasses import dataclass, field

PREFIXES = {
    "rdf": "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
    "dcterms": "http://purl.org/dc/terms/",
    "oslc": "http://open-services.net/ns/core#",
    "oslc_cm": "http://open-services.net/ns/cm#",
    "rdfs": "http://www.w3.org/2000/01/rdf-schema#",
    "foaf": "http://xmlns.com/foaf/0.1/",
    "cq": "http://www.ibm.com/xmlns/prod/rational/clearquest/1.0/",
    "oslc_cmx": "http://open-services.net/ns/cm-x#",
}

TYPE_MAP = {
    "http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral": "str",
    "http://www.w3.org/2001/XMLSchema#boolean": "bool",
    "http://www.w3.org/2001/XMLSchema#dateTime": "datetime",
    "http://www.w3.org/2001/XMLSchema#decimal": "float",
    "http://www.w3.org/2001/XMLSchema#double": "float",
    "http://www.w3.org/2001/XMLSchema#float": "float",
    "http://www.w3.org/2001/XMLSchema#integer": "int",
    "http://www.w3.org/2001/XMLSchema#string": "str",
    "http://www.w3.org/1999/02/22-rdf-syntax-ns#langString": "str",
    "http://open-services.net/ns/core#Resource": "Resource",
    "http://open-services.net/ns/core#AnyResource": "Resource",
    "http://open-services.net/ns/core#LocalResource": "Resource",
}


class Occurrence:
    ZERO_OR_ONE = "http://open-services.net/ns/core#Zero-or-one"
    EXACTLY_ONE = "http://open-services.net/ns/core#Exactly-one"
    ZERO_OR_MANY = "http://open-services.net/ns/core#Zero-or-many"
    ONE_OR_MANY = "http://open-services.net/ns/core#One-or-many"


class Representation:
    EITHER = "http://open-services.net/ns/core#Either"
    INLINE = "http://open-services.net/ns/core#Inline"
    REFERENCE = "http://open-services.net/ns/core#Reference"


@dataclass
class Reference:
    ref: str

    def __hash__(self):
        return hash(self.ref)


@dataclass
class Property:
    name: str
    occurs: Occurrence
    propertyDefinition: Reference
    description: Optional[str] = None
    title: Optional[str] = None
    allowedValues: Optional[List[Any]] = None
    defaultValue: Optional[Any] = None
    hidden: Optional[bool] = None
    isMemberProperty: Optional[bool] = None
    maxSize: Optional[int] = None
    queryable: Optional[bool] = None
    range: List[Reference] = field(default_factory=lambda: [])
    readOnly: Optional[bool] = None
    representation: Optional[Representation] = None
    valueShape: Optional[Reference] = None
    valueType: list[Reference] = field(default_factory=lambda: [])


@dataclass
class ResourceShape:
    description: Optional[str] = None
    title: Optional[str] = None
    describes: List[Reference] = field(default_factory=lambda: [])
    hidden: Optional[bool] = None
    properties: List[Property] = field(default_factory=lambda: [])


def to_python_type_str(t: str) -> str:
    if t in TYPE_MAP:
        return TYPE_MAP[t]
    else:
        return "typing.Any"
