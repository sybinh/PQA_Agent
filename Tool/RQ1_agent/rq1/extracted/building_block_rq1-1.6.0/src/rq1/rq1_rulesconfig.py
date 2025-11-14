
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



class Rq1_Rulesconfig(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    actionname: typing.Optional[str] = Field(alias="cq:ActionName", default=None)
    
    actiontype: typing.Optional[str] = Field(alias="cq:ActionType", default=None)
    
    build: typing.Optional[typing.Literal["stable", "test"]] = Field(alias="cq:Build", default=None)
    
    cfgversion: typing.Optional[str] = Field(alias="cq:CFGVersion", default=None)
    
    changeable: typing.Optional[int] = Field(alias="cq:Changeable", default=None)
    
    counting: typing.Optional[int] = Field(alias="cq:Counting", default=None)
    
    cq__Description: typing.Optional[str] = Field(alias="cq:Description", default=None)
    
    datasource: typing.Optional[str] = Field(alias="cq:DataSource", default=None)
    
    datasourcevalue: typing.Optional[str] = Field(alias="cq:DataSourceValue", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__description: typing.Optional[str] = Field(alias="dcterms:description", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    dependentfields: typing.Optional[str] = Field(alias="cq:DependentFields", default=None)
    
    developerid: typing.Optional[str] = Field(alias="cq:DeveloperID", default=None)
    
    disabledinversion: typing.Optional[str] = Field(alias="cq:DisabledInVersion", default=None)
    
    enabled: typing.Optional[int] = Field(alias="cq:Enabled", default=None)
    
    executionorder: typing.Optional[int] = Field(alias="cq:ExecutionOrder", default=None)
    
    expression1: typing.Optional[str] = Field(alias="cq:Expression1", default=None)
    
    expression2: typing.Optional[str] = Field(alias="cq:Expression2", default=None)
    
    fieldname: typing.Optional[str] = Field(alias="cq:FieldName", default=None)
    
    forusers: typing.Optional[str] = Field(alias="cq:ForUsers", default=None)
    
    hasacl: typing.Optional[typing.Union[Resource, 'Commercialacl']] = Field(alias="cq:hasACL", default=None)
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    historylog: typing.Optional[str] = Field(alias="cq:HistoryLog", default=None)
    
    hooktype: typing.Optional[str] = Field(alias="cq:HookType", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    instructiontype: typing.Optional[str] = Field(alias="cq:InstructionType", default=None)
    
    instructiontypedesc: typing.Optional[str] = Field(alias="cq:InstructionTypeDesc", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    lastmodifieddate: typing.Optional[datetime] = Field(alias="cq:LastModifiedDate", default=None)
    
    lastmodifieduser: typing.Optional[str] = Field(alias="cq:LastModifiedUser", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    name: typing.Optional[str] = Field(alias="cq:Name", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    recordscriptname: typing.Optional[str] = Field(alias="cq:RecordScriptName", default=None)
    
    recordtype: typing.Optional[str] = Field(alias="cq:RecordType", default=None)
    
    relatedfields: typing.Optional[str] = Field(alias="cq:RelatedFields", default=None)
    
    revision: typing.Optional[int] = Field(alias="cq:Revision", default=None)
    
    shorttitle: typing.Optional[str] = Field(alias="oslc:shortTitle", default=None)
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    tld: typing.Optional[str] = Field(alias="cq:TLD", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16777239"

    @classmethod
    def shape_name(cls) -> str:
        return "RQ1_RulesConfig"

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
    


class Rq1_RulesconfigProperty(Enum):
    actionname = "cq:ActionName"
    actiontype = "cq:ActionType"
    build = "cq:Build"
    cfgversion = "cq:CFGVersion"
    changeable = "cq:Changeable"
    counting = "cq:Counting"
    cq__Description = "cq:Description"
    datasource = "cq:DataSource"
    datasourcevalue = "cq:DataSourceValue"
    dbid = "cq:dbid"
    dcterms__description = "dcterms:description"
    dcterms__type = "dcterms:type"
    dependentfields = "cq:DependentFields"
    developerid = "cq:DeveloperID"
    disabledinversion = "cq:DisabledInVersion"
    enabled = "cq:Enabled"
    executionorder = "cq:ExecutionOrder"
    expression1 = "cq:Expression1"
    expression2 = "cq:Expression2"
    fieldname = "cq:FieldName"
    forusers = "cq:ForUsers"
    hasacl = "cq:hasACL"
    history = "cq:history"
    historylog = "cq:HistoryLog"
    hooktype = "cq:HookType"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    instructiontype = "cq:InstructionType"
    instructiontypedesc = "cq:InstructionTypeDesc"
    is_active = "cq:is_active"
    lastmodifieddate = "cq:LastModifiedDate"
    lastmodifieduser = "cq:LastModifiedUser"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    name = "cq:Name"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    recordscriptname = "cq:RecordScriptName"
    recordtype = "cq:RecordType"
    relatedfields = "cq:RelatedFields"
    revision = "cq:Revision"
    shorttitle = "oslc:shortTitle"
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


def create_rq1_rulesconfig_changeset(
    actionname: typing.Optional[str] = None,
    actiontype: typing.Optional[str] = None,
    build: typing.Optional[typing.Literal["stable", "test"]] = None,
    cfgversion: typing.Optional[str] = None,
    changeable: typing.Optional[int] = None,
    counting: typing.Optional[int] = None,
    cq__Description: typing.Optional[str] = None,
    datasource: typing.Optional[str] = None,
    datasourcevalue: typing.Optional[str] = None,
    dbid: typing.Optional[str] = None,
    dcterms__description: typing.Optional[str] = None,
    dependentfields: typing.Optional[str] = None,
    developerid: typing.Optional[str] = None,
    disabledinversion: typing.Optional[str] = None,
    enabled: typing.Optional[int] = None,
    executionorder: typing.Optional[int] = None,
    expression1: typing.Optional[str] = None,
    expression2: typing.Optional[str] = None,
    fieldname: typing.Optional[str] = None,
    forusers: typing.Optional[str] = None,
    hasacl: typing.Optional[typing.Union[Resource, 'Commercialacl']] = None,
    history: typing.Optional[Resource] = None,
    historylog: typing.Optional[str] = None,
    hooktype: typing.Optional[str] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    instructiontype: typing.Optional[str] = None,
    instructiontypedesc: typing.Optional[str] = None,
    is_active: typing.Optional[int] = None,
    lastmodifieddate: typing.Optional[datetime] = None,
    lastmodifieduser: typing.Optional[str] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    name: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    recordscriptname: typing.Optional[str] = None,
    recordtype: typing.Optional[str] = None,
    relatedfields: typing.Optional[str] = None,
    revision: typing.Optional[int] = None,
    shorttitle: typing.Optional[str] = None,
    title: typing.Optional[str] = None,
    tld: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    
) -> Rq1_Rulesconfig:
    '''
    Create a Rq1_Rulesconfig changeset.
    '''
    fields = {}
    if actionname is not None:
        fields["cq:ActionName"] = actionname 
    if actiontype is not None:
        fields["cq:ActionType"] = actiontype 
    if build is not None:
        fields["cq:Build"] = build 
    if cfgversion is not None:
        fields["cq:CFGVersion"] = cfgversion 
    if changeable is not None:
        fields["cq:Changeable"] = changeable 
    if counting is not None:
        fields["cq:Counting"] = counting 
    if cq__Description is not None:
        fields["cq:Description"] = cq__Description 
    if datasource is not None:
        fields["cq:DataSource"] = datasource 
    if datasourcevalue is not None:
        fields["cq:DataSourceValue"] = datasourcevalue 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if dcterms__description is not None:
        fields["dcterms:description"] = dcterms__description 
    if dependentfields is not None:
        fields["cq:DependentFields"] = dependentfields 
    if developerid is not None:
        fields["cq:DeveloperID"] = developerid 
    if disabledinversion is not None:
        fields["cq:DisabledInVersion"] = disabledinversion 
    if enabled is not None:
        fields["cq:Enabled"] = enabled 
    if executionorder is not None:
        fields["cq:ExecutionOrder"] = executionorder 
    if expression1 is not None:
        fields["cq:Expression1"] = expression1 
    if expression2 is not None:
        fields["cq:Expression2"] = expression2 
    if fieldname is not None:
        fields["cq:FieldName"] = fieldname 
    if forusers is not None:
        fields["cq:ForUsers"] = forusers 
    if hasacl is not None:
        fields["cq:hasACL"] = hasacl 
    if history is not None:
        fields["cq:history"] = history 
    if historylog is not None:
        fields["cq:HistoryLog"] = historylog 
    if hooktype is not None:
        fields["cq:HookType"] = hooktype 
    if identifier is not None:
        fields["dcterms:identifier"] = identifier 
    if instanceshape is not None:
        fields["oslc:instanceShape"] = instanceshape 
    if instructiontype is not None:
        fields["cq:InstructionType"] = instructiontype 
    if instructiontypedesc is not None:
        fields["cq:InstructionTypeDesc"] = instructiontypedesc 
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
    if name is not None:
        fields["cq:Name"] = name 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if recordscriptname is not None:
        fields["cq:RecordScriptName"] = recordscriptname 
    if recordtype is not None:
        fields["cq:RecordType"] = recordtype 
    if relatedfields is not None:
        fields["cq:RelatedFields"] = relatedfields 
    if revision is not None:
        fields["cq:Revision"] = revision 
    if shorttitle is not None:
        fields["oslc:shortTitle"] = shorttitle 
    if title is not None:
        fields["dcterms:title"] = title 
    if tld is not None:
        fields["cq:TLD"] = tld 
    if version is not None:
        fields["cq:version"] = version 
    

    fields["dcterms:type"] = "RQ1_RulesConfig"
    return Rq1_Rulesconfig(**fields)