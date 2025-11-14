
from .base import Resource, QueryStatement, BaseRecord
from pydantic import ConfigDict, Field, field_validator
import typing
from datetime import datetime, timezone
from enum import Enum
import json


from typing import TYPE_CHECKING

if TYPE_CHECKING:

    from .ratl_replicas import Ratl_Replicas

    from .commercialacl import Commercialacl

    from .users import Users



class Rq1_Rolespermission(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    behavior: typing.Optional[int] = Field(alias="cq:Behavior", default=None)
    
    cfgversion: typing.Optional[str] = Field(alias="cq:CFGVersion", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    domain: typing.Optional[str] = Field(alias="cq:Domain", default=None)
    
    enabled: typing.Optional[int] = Field(alias="cq:Enabled", default=None)
    
    fieldname: typing.Optional[str] = Field(alias="cq:FieldName", default=None)
    
    fromstate: typing.Optional[str] = Field(alias="cq:FromState", default=None)
    
    hasacl: typing.Optional[typing.Union[Resource, 'Commercialacl']] = Field(alias="cq:hasACL", default=None)
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    recordtype: typing.Optional[str] = Field(alias="cq:RecordType", default=None)
    
    rolename: typing.Optional[str] = Field(alias="cq:RoleName", default=None)
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    tld: typing.Optional[str] = Field(alias="cq:TLD", default=None)
    
    tostate: typing.Optional[str] = Field(alias="cq:ToState", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16777241"

    @classmethod
    def shape_name(cls) -> str:
        return "RQ1_RolesPermission"

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
    


class Rq1_RolespermissionProperty(Enum):
    behavior = "cq:Behavior"
    cfgversion = "cq:CFGVersion"
    dbid = "cq:dbid"
    dcterms__type = "dcterms:type"
    domain = "cq:Domain"
    enabled = "cq:Enabled"
    fieldname = "cq:FieldName"
    fromstate = "cq:FromState"
    hasacl = "cq:hasACL"
    history = "cq:history"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    is_active = "cq:is_active"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    recordtype = "cq:RecordType"
    rolename = "cq:RoleName"
    title = "dcterms:title"
    tld = "cq:TLD"
    tostate = "cq:ToState"
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


def create_rq1_rolespermission_changeset(
    behavior: typing.Optional[int] = None,
    cfgversion: typing.Optional[str] = None,
    dbid: typing.Optional[str] = None,
    domain: typing.Optional[str] = None,
    enabled: typing.Optional[int] = None,
    fieldname: typing.Optional[str] = None,
    fromstate: typing.Optional[str] = None,
    hasacl: typing.Optional[typing.Union[Resource, 'Commercialacl']] = None,
    history: typing.Optional[Resource] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    is_active: typing.Optional[int] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    recordtype: typing.Optional[str] = None,
    rolename: typing.Optional[str] = None,
    title: typing.Optional[str] = None,
    tld: typing.Optional[str] = None,
    tostate: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    
) -> Rq1_Rolespermission:
    '''
    Create a Rq1_Rolespermission changeset.
    '''
    fields = {}
    if behavior is not None:
        fields["cq:Behavior"] = behavior 
    if cfgversion is not None:
        fields["cq:CFGVersion"] = cfgversion 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if domain is not None:
        fields["cq:Domain"] = domain 
    if enabled is not None:
        fields["cq:Enabled"] = enabled 
    if fieldname is not None:
        fields["cq:FieldName"] = fieldname 
    if fromstate is not None:
        fields["cq:FromState"] = fromstate 
    if hasacl is not None:
        fields["cq:hasACL"] = hasacl 
    if history is not None:
        fields["cq:history"] = history 
    if identifier is not None:
        fields["dcterms:identifier"] = identifier 
    if instanceshape is not None:
        fields["oslc:instanceShape"] = instanceshape 
    if is_active is not None:
        fields["cq:is_active"] = is_active 
    if lock_version is not None:
        fields["cq:lock_version"] = lock_version 
    if locked_by is not None:
        fields["cq:locked_by"] = locked_by 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if recordtype is not None:
        fields["cq:RecordType"] = recordtype 
    if rolename is not None:
        fields["cq:RoleName"] = rolename 
    if title is not None:
        fields["dcterms:title"] = title 
    if tld is not None:
        fields["cq:TLD"] = tld 
    if tostate is not None:
        fields["cq:ToState"] = tostate 
    if version is not None:
        fields["cq:version"] = version 
    

    fields["dcterms:type"] = "RQ1_RolesPermission"
    return Rq1_Rolespermission(**fields)