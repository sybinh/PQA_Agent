
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



class Rq1_Instructiontype(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    cfgversion: typing.Optional[str] = Field(alias="cq:CFGVersion", default=None)
    
    changeable: typing.Optional[int] = Field(alias="cq:Changeable", default=None)
    
    code_stable: typing.Optional[str] = Field(alias="cq:code_stable", default=None)
    
    code_test: typing.Optional[str] = Field(alias="cq:code_test", default=None)
    
    cq__Description: typing.Optional[str] = Field(alias="cq:Description", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__description: typing.Optional[str] = Field(alias="dcterms:description", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    debuglevel: typing.Optional[int] = Field(alias="cq:DebugLevel", default=None)
    
    developerid: typing.Optional[str] = Field(alias="cq:DeveloperID", default=None)
    
    docrelevant: typing.Optional[int] = Field(alias="cq:DocRelevant", default=None)
    
    enabled: typing.Optional[int] = Field(alias="cq:Enabled", default=None)
    
    executionorder: typing.Optional[int] = Field(alias="cq:ExecutionOrder", default=None)
    
    hasacl: typing.Optional[typing.Union[Resource, 'Commercialacl']] = Field(alias="cq:hasACL", default=None)
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    historylog: typing.Optional[str] = Field(alias="cq:HistoryLog", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    instructiontype: typing.Optional[str] = Field(alias="cq:InstructionType", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    lastmodifieddate: typing.Optional[datetime] = Field(alias="cq:LastModifiedDate", default=None)
    
    lastmodifieduser: typing.Optional[str] = Field(alias="cq:LastModifiedUser", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    longdescription: typing.Optional[str] = Field(alias="cq:LongDescription", default=None)
    
    mandatory: typing.Optional[str] = Field(alias="cq:Mandatory", default=None)
    
    optional: typing.Optional[str] = Field(alias="cq:Optional", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    returntype: typing.Optional[typing.Literal["boolean", "error", "list", "undef", "value"]] = Field(alias="cq:ReturnType", default=None)
    
    revision: typing.Optional[int] = Field(alias="cq:Revision", default=None)
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    tld: typing.Optional[str] = Field(alias="cq:TLD", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16781800"

    @classmethod
    def shape_name(cls) -> str:
        return "RQ1_InstructionType"

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
    


class Rq1_InstructiontypeProperty(Enum):
    cfgversion = "cq:CFGVersion"
    changeable = "cq:Changeable"
    code_stable = "cq:code_stable"
    code_test = "cq:code_test"
    cq__Description = "cq:Description"
    dbid = "cq:dbid"
    dcterms__description = "dcterms:description"
    dcterms__type = "dcterms:type"
    debuglevel = "cq:DebugLevel"
    developerid = "cq:DeveloperID"
    docrelevant = "cq:DocRelevant"
    enabled = "cq:Enabled"
    executionorder = "cq:ExecutionOrder"
    hasacl = "cq:hasACL"
    history = "cq:history"
    historylog = "cq:HistoryLog"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    instructiontype = "cq:InstructionType"
    is_active = "cq:is_active"
    lastmodifieddate = "cq:LastModifiedDate"
    lastmodifieduser = "cq:LastModifiedUser"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    longdescription = "cq:LongDescription"
    mandatory = "cq:Mandatory"
    optional = "cq:Optional"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    returntype = "cq:ReturnType"
    revision = "cq:Revision"
    title = "dcterms:title"
    tld = "cq:TLD"
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


def create_rq1_instructiontype_changeset(
    cfgversion: typing.Optional[str] = None,
    changeable: typing.Optional[int] = None,
    code_stable: typing.Optional[str] = None,
    code_test: typing.Optional[str] = None,
    cq__Description: typing.Optional[str] = None,
    dbid: typing.Optional[str] = None,
    dcterms__description: typing.Optional[str] = None,
    debuglevel: typing.Optional[int] = None,
    developerid: typing.Optional[str] = None,
    docrelevant: typing.Optional[int] = None,
    enabled: typing.Optional[int] = None,
    executionorder: typing.Optional[int] = None,
    hasacl: typing.Optional[typing.Union[Resource, 'Commercialacl']] = None,
    history: typing.Optional[Resource] = None,
    historylog: typing.Optional[str] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    instructiontype: typing.Optional[str] = None,
    is_active: typing.Optional[int] = None,
    lastmodifieddate: typing.Optional[datetime] = None,
    lastmodifieduser: typing.Optional[str] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    longdescription: typing.Optional[str] = None,
    mandatory: typing.Optional[str] = None,
    optional: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    returntype: typing.Optional[typing.Literal["boolean", "error", "list", "undef", "value"]] = None,
    revision: typing.Optional[int] = None,
    title: typing.Optional[str] = None,
    tld: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    
) -> Rq1_Instructiontype:
    '''
    Create a Rq1_Instructiontype changeset.
    '''
    fields = {}
    if cfgversion is not None:
        fields["cq:CFGVersion"] = cfgversion 
    if changeable is not None:
        fields["cq:Changeable"] = changeable 
    if code_stable is not None:
        fields["cq:code_stable"] = code_stable 
    if code_test is not None:
        fields["cq:code_test"] = code_test 
    if cq__Description is not None:
        fields["cq:Description"] = cq__Description 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if dcterms__description is not None:
        fields["dcterms:description"] = dcterms__description 
    if debuglevel is not None:
        fields["cq:DebugLevel"] = debuglevel 
    if developerid is not None:
        fields["cq:DeveloperID"] = developerid 
    if docrelevant is not None:
        fields["cq:DocRelevant"] = docrelevant 
    if enabled is not None:
        fields["cq:Enabled"] = enabled 
    if executionorder is not None:
        fields["cq:ExecutionOrder"] = executionorder 
    if hasacl is not None:
        fields["cq:hasACL"] = hasacl 
    if history is not None:
        fields["cq:history"] = history 
    if historylog is not None:
        fields["cq:HistoryLog"] = historylog 
    if identifier is not None:
        fields["dcterms:identifier"] = identifier 
    if instanceshape is not None:
        fields["oslc:instanceShape"] = instanceshape 
    if instructiontype is not None:
        fields["cq:InstructionType"] = instructiontype 
    if is_active is not None:
        fields["cq:is_active"] = is_active 
    if lastmodifieddate is not None:
        fields["cq:LastModifiedDate"] = lastmodifieddate 
    if lastmodifieduser is not None:
        fields["cq:LastModifiedUser"] = lastmodifieduser 
    if lock_version is not None:
        fields["cq:lock_version"] = lock_version 
    if locked_by is not None:
        fields["cq:locked_by"] = locked_by 
    if longdescription is not None:
        fields["cq:LongDescription"] = longdescription 
    if mandatory is not None:
        fields["cq:Mandatory"] = mandatory 
    if optional is not None:
        fields["cq:Optional"] = optional 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if returntype is not None:
        fields["cq:ReturnType"] = returntype 
    if revision is not None:
        fields["cq:Revision"] = revision 
    if title is not None:
        fields["dcterms:title"] = title 
    if tld is not None:
        fields["cq:TLD"] = tld 
    if version is not None:
        fields["cq:version"] = version 
    

    fields["dcterms:type"] = "RQ1_InstructionType"
    return Rq1_Instructiontype(**fields)