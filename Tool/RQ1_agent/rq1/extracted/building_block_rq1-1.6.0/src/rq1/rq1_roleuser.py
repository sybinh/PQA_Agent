
from .base import Resource, QueryStatement, BaseRecord
from pydantic import ConfigDict, Field, field_validator
import typing
from datetime import datetime, timezone
from enum import Enum
import json


from typing import TYPE_CHECKING

if TYPE_CHECKING:

    from .ratl_replicas import Ratl_Replicas

    from .project import Project

    from .users import Users



class Rq1_Roleuser(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    creationcontext: typing.Optional[str] = Field(alias="cq:CreationContext", default=None)
    
    creationmode: typing.Optional[str] = Field(alias="cq:CreationMode", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    id: typing.Optional[str] = Field(alias="cq:id", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    isprojectmember: typing.Optional[typing.Union[Resource, 'Project']] = Field(alias="cq:isProjectMember", default=None)
    
    lastmodifieddate: typing.Optional[datetime] = Field(alias="cq:LastModifiedDate", default=None)
    
    lastmodifieduser: typing.Optional[str] = Field(alias="cq:LastModifiedUser", default=None)
    
    linkeduser: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:LinkedUser", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    operationcontext: typing.Optional[str] = Field(alias="cq:OperationContext", default=None)
    
    operationmode: typing.Optional[str] = Field(alias="cq:OperationMode", default=None)
    
    projectid: typing.Optional[str] = Field(alias="cq:ProjectID", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    rolename: typing.Optional[str] = Field(alias="cq:RoleName", default=None)
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    tld: typing.Optional[str] = Field(alias="cq:TLD", default=None)
    
    userid: typing.Optional[str] = Field(alias="cq:UserID", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16777237"

    @classmethod
    def shape_name(cls) -> str:
        return "RQ1_RoleUser"

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
    


class Rq1_RoleuserProperty(Enum):
    creationcontext = "cq:CreationContext"
    creationmode = "cq:CreationMode"
    dbid = "cq:dbid"
    dcterms__type = "dcterms:type"
    history = "cq:history"
    id = "cq:id"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    is_active = "cq:is_active"
    isprojectmember = "cq:isProjectMember"
    lastmodifieddate = "cq:LastModifiedDate"
    lastmodifieduser = "cq:LastModifiedUser"
    linkeduser = "cq:LinkedUser"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    operationcontext = "cq:OperationContext"
    operationmode = "cq:OperationMode"
    projectid = "cq:ProjectID"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    rolename = "cq:RoleName"
    title = "dcterms:title"
    tld = "cq:TLD"
    userid = "cq:UserID"
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


def create_rq1_roleuser_changeset(
    creationcontext: typing.Optional[str] = None,
    creationmode: typing.Optional[str] = None,
    dbid: typing.Optional[str] = None,
    history: typing.Optional[Resource] = None,
    id: typing.Optional[str] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    is_active: typing.Optional[int] = None,
    isprojectmember: typing.Optional[typing.Union[Resource, 'Project']] = None,
    lastmodifieddate: typing.Optional[datetime] = None,
    lastmodifieduser: typing.Optional[str] = None,
    linkeduser: typing.Optional[typing.Union[Resource, 'Users']] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    operationcontext: typing.Optional[str] = None,
    operationmode: typing.Optional[str] = None,
    projectid: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    rolename: typing.Optional[str] = None,
    title: typing.Optional[str] = None,
    tld: typing.Optional[str] = None,
    userid: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    
) -> Rq1_Roleuser:
    '''
    Create a Rq1_Roleuser changeset.
    '''
    fields = {}
    if creationcontext is not None:
        fields["cq:CreationContext"] = creationcontext 
    if creationmode is not None:
        fields["cq:CreationMode"] = creationmode 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if history is not None:
        fields["cq:history"] = history 
    if id is not None:
        fields["cq:id"] = id 
    if identifier is not None:
        fields["dcterms:identifier"] = identifier 
    if instanceshape is not None:
        fields["oslc:instanceShape"] = instanceshape 
    if is_active is not None:
        fields["cq:is_active"] = is_active 
    if isprojectmember is not None:
        fields["cq:isProjectMember"] = isprojectmember 
    if lastmodifieddate is not None:
        fields["cq:LastModifiedDate"] = lastmodifieddate 
    if lastmodifieduser is not None:
        fields["cq:LastModifiedUser"] = lastmodifieduser 
    if linkeduser is not None:
        fields["cq:LinkedUser"] = linkeduser 
    if lock_version is not None:
        fields["cq:lock_version"] = lock_version 
    if locked_by is not None:
        fields["cq:locked_by"] = locked_by 
    if operationcontext is not None:
        fields["cq:OperationContext"] = operationcontext 
    if operationmode is not None:
        fields["cq:OperationMode"] = operationmode 
    if projectid is not None:
        fields["cq:ProjectID"] = projectid 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if rolename is not None:
        fields["cq:RoleName"] = rolename 
    if title is not None:
        fields["dcterms:title"] = title 
    if tld is not None:
        fields["cq:TLD"] = tld 
    if userid is not None:
        fields["cq:UserID"] = userid 
    if version is not None:
        fields["cq:version"] = version 
    

    fields["dcterms:type"] = "RQ1_RoleUser"
    return Rq1_Roleuser(**fields)