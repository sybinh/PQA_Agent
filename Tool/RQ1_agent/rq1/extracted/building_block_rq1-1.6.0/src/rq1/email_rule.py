
from .base import Resource, QueryStatement, BaseRecord
from pydantic import ConfigDict, Field, field_validator
import typing
from datetime import datetime, timezone
from enum import Enum
import json


from typing import TYPE_CHECKING

if TYPE_CHECKING:

    from .ratl_replicas import Ratl_Replicas

    from .groups import Groups

    from .users import Users



class Email_Rule(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    action_types: typing.Optional[str] = Field(alias="cq:Action_Types", default=None)
    
    actions: typing.Optional[str] = Field(alias="cq:Actions", default=None)
    
    cc_actioner: typing.Optional[int] = Field(alias="cq:CC_Actioner", default=None)
    
    cc_additional: typing.Optional[str] = Field(alias="cq:CC_Additional", default=None)
    
    cc_addr_fields: typing.Optional[str] = Field(alias="cq:CC_Addr_Fields", default=None)
    
    cc_groups: typing.List[typing.Union[Resource, 'Groups']] = Field(alias="cq:CC_Groups", default_factory=lambda: [])
    
    cc_users: typing.List[typing.Union[Resource, 'Users']] = Field(alias="cq:CC_Users", default_factory=lambda: [])
    
    change_fields: typing.Optional[str] = Field(alias="cq:Change_Fields", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    display_fields: typing.Optional[str] = Field(alias="cq:Display_Fields", default=None)
    
    entity_def: typing.Optional[str] = Field(alias="cq:Entity_Def", default=None)
    
    filter_query: typing.Optional[str] = Field(alias="cq:Filter_Query", default=None)
    
    from_addr: typing.Optional[str] = Field(alias="cq:From_Addr", default=None)
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    include_defect: typing.Optional[int] = Field(alias="cq:Include_Defect", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    is_active_rule: typing.Optional[int] = Field(alias="cq:Is_Active_Rule", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    name: typing.Optional[str] = Field(alias="cq:Name", default=None)
    
    operator_value: typing.Optional[int] = Field(alias="cq:Operator_Value", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    shorttitle: typing.Optional[str] = Field(alias="oslc:shortTitle", default=None)
    
    show_previous: typing.Optional[int] = Field(alias="cq:Show_Previous", default=None)
    
    source_states: typing.Optional[str] = Field(alias="cq:Source_States", default=None)
    
    subject_fields: typing.Optional[str] = Field(alias="cq:Subject_Fields", default=None)
    
    target_states: typing.Optional[str] = Field(alias="cq:Target_States", default=None)
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    to_additional: typing.Optional[str] = Field(alias="cq:To_Additional", default=None)
    
    to_addr_fields: typing.Optional[str] = Field(alias="cq:To_Addr_Fields", default=None)
    
    to_groups: typing.List[typing.Union[Resource, 'Groups']] = Field(alias="cq:To_Groups", default_factory=lambda: [])
    
    to_users: typing.List[typing.Union[Resource, 'Users']] = Field(alias="cq:To_Users", default_factory=lambda: [])
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16777235"

    @classmethod
    def shape_name(cls) -> str:
        return "Email_Rule"

    @field_validator('*', mode="before")
    @classmethod
    def empty_str_to_none(cls, v):
        if v == '':
            return None
        return v

    
    @field_validator('instanceshape', 'history', mode="before")
    @classmethod
    def list_to_resource(cls, v):
        if isinstance(v, list):
            return v[0]
        return v
    


class Email_RuleProperty(Enum):
    action_types = "cq:Action_Types"
    actions = "cq:Actions"
    cc_actioner = "cq:CC_Actioner"
    cc_additional = "cq:CC_Additional"
    cc_addr_fields = "cq:CC_Addr_Fields"
    cc_groups = "cq:CC_Groups"
    cc_users = "cq:CC_Users"
    change_fields = "cq:Change_Fields"
    dbid = "cq:dbid"
    dcterms__type = "dcterms:type"
    display_fields = "cq:Display_Fields"
    entity_def = "cq:Entity_Def"
    filter_query = "cq:Filter_Query"
    from_addr = "cq:From_Addr"
    history = "cq:history"
    identifier = "dcterms:identifier"
    include_defect = "cq:Include_Defect"
    instanceshape = "oslc:instanceShape"
    is_active = "cq:is_active"
    is_active_rule = "cq:Is_Active_Rule"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    name = "cq:Name"
    operator_value = "cq:Operator_Value"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    shorttitle = "oslc:shortTitle"
    show_previous = "cq:Show_Previous"
    source_states = "cq:Source_States"
    subject_fields = "cq:Subject_Fields"
    target_states = "cq:Target_States"
    title = "dcterms:title"
    to_additional = "cq:To_Additional"
    to_addr_fields = "cq:To_Addr_Fields"
    to_groups = "cq:To_Groups"
    to_users = "cq:To_Users"
    version = "cq:version"
    

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
            return QueryStatement(self.value + "{" + str(value) + "}")
        else:
            return QueryStatement(self.value + "{" + ",".join([str(x) for x in value]) + "}")

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


def create_email_rule_changeset(
    action_types: typing.Optional[str] = None,
    actions: typing.Optional[str] = None,
    cc_actioner: typing.Optional[int] = None,
    cc_additional: typing.Optional[str] = None,
    cc_addr_fields: typing.Optional[str] = None,
    cc_groups: typing.Optional[typing.List[typing.Union[Resource, 'Groups']]] = None,
    cc_users: typing.Optional[typing.List[typing.Union[Resource, 'Users']]] = None,
    change_fields: typing.Optional[str] = None,
    dbid: typing.Optional[str] = None,
    display_fields: typing.Optional[str] = None,
    entity_def: typing.Optional[str] = None,
    filter_query: typing.Optional[str] = None,
    from_addr: typing.Optional[str] = None,
    history: typing.Optional[Resource] = None,
    identifier: typing.Optional[str] = None,
    include_defect: typing.Optional[int] = None,
    instanceshape: typing.Optional[Resource] = None,
    is_active: typing.Optional[int] = None,
    is_active_rule: typing.Optional[int] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    name: typing.Optional[str] = None,
    operator_value: typing.Optional[int] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    shorttitle: typing.Optional[str] = None,
    show_previous: typing.Optional[int] = None,
    source_states: typing.Optional[str] = None,
    subject_fields: typing.Optional[str] = None,
    target_states: typing.Optional[str] = None,
    title: typing.Optional[str] = None,
    to_additional: typing.Optional[str] = None,
    to_addr_fields: typing.Optional[str] = None,
    to_groups: typing.Optional[typing.List[typing.Union[Resource, 'Groups']]] = None,
    to_users: typing.Optional[typing.List[typing.Union[Resource, 'Users']]] = None,
    version: typing.Optional[int] = None,
    
) -> Email_Rule:
    '''
    Create a Email_Rule changeset.
    '''
    fields = {}
    if action_types is not None:
        fields["cq:Action_Types"] = action_types 
    if actions is not None:
        fields["cq:Actions"] = actions 
    if cc_actioner is not None:
        fields["cq:CC_Actioner"] = cc_actioner 
    if cc_additional is not None:
        fields["cq:CC_Additional"] = cc_additional 
    if cc_addr_fields is not None:
        fields["cq:CC_Addr_Fields"] = cc_addr_fields 
    if cc_groups is not None:
        fields["cq:CC_Groups"] = cc_groups 
    if cc_users is not None:
        fields["cq:CC_Users"] = cc_users 
    if change_fields is not None:
        fields["cq:Change_Fields"] = change_fields 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if display_fields is not None:
        fields["cq:Display_Fields"] = display_fields 
    if entity_def is not None:
        fields["cq:Entity_Def"] = entity_def 
    if filter_query is not None:
        fields["cq:Filter_Query"] = filter_query 
    if from_addr is not None:
        fields["cq:From_Addr"] = from_addr 
    if history is not None:
        fields["cq:history"] = history 
    if identifier is not None:
        fields["dcterms:identifier"] = identifier 
    if include_defect is not None:
        fields["cq:Include_Defect"] = include_defect 
    if instanceshape is not None:
        fields["oslc:instanceShape"] = instanceshape 
    if is_active is not None:
        fields["cq:is_active"] = is_active 
    if is_active_rule is not None:
        fields["cq:Is_Active_Rule"] = is_active_rule 
    if lock_version is not None:
        fields["cq:lock_version"] = lock_version 
    if locked_by is not None:
        fields["cq:locked_by"] = locked_by 
    if name is not None:
        fields["cq:Name"] = name 
    if operator_value is not None:
        fields["cq:Operator_Value"] = operator_value 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if shorttitle is not None:
        fields["oslc:shortTitle"] = shorttitle 
    if show_previous is not None:
        fields["cq:Show_Previous"] = show_previous 
    if source_states is not None:
        fields["cq:Source_States"] = source_states 
    if subject_fields is not None:
        fields["cq:Subject_Fields"] = subject_fields 
    if target_states is not None:
        fields["cq:Target_States"] = target_states 
    if title is not None:
        fields["dcterms:title"] = title 
    if to_additional is not None:
        fields["cq:To_Additional"] = to_additional 
    if to_addr_fields is not None:
        fields["cq:To_Addr_Fields"] = to_addr_fields 
    if to_groups is not None:
        fields["cq:To_Groups"] = to_groups 
    if to_users is not None:
        fields["cq:To_Users"] = to_users 
    if version is not None:
        fields["cq:version"] = version 
    

    fields["dcterms:type"] = "Email_Rule"
    return Email_Rule(**fields)