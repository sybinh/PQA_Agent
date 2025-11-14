
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



class Rq1_Logging(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    actionname: typing.Optional[str] = Field(alias="cq:ActionName", default=None)
    
    afilename: typing.Optional[str] = Field(alias="cq:AFileName", default=None)
    
    asamdebug: typing.Optional[str] = Field(alias="cq:ASAMDebug", default=None)
    
    beginsection: typing.Optional[str] = Field(alias="cq:BeginSection", default=None)
    
    cq__Status: typing.Optional[str] = Field(alias="cq:Status", default=None)
    
    currentfiles: typing.Optional[str] = Field(alias="cq:CurrentFiles", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    debuginstruction: typing.Optional[str] = Field(alias="cq:DebugInstruction", default=None)
    
    debuglevel: typing.Optional[str] = Field(alias="cq:DebugLevel", default=None)
    
    error: typing.Optional[str] = Field(alias="cq:Error", default=None)
    
    fieldname: typing.Optional[str] = Field(alias="cq:FieldName", default=None)
    
    filecontent: typing.Optional[str] = Field(alias="cq:FileContent", default=None)
    
    folder: typing.Optional[str] = Field(alias="cq:Folder", default=None)
    
    hasacl: typing.Optional[typing.Union[Resource, 'Commercialacl']] = Field(alias="cq:hasACL", default=None)
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    hook: typing.Optional[str] = Field(alias="cq:Hook", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    instructiontype: typing.Optional[str] = Field(alias="cq:InstructionType", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    lastmodifieddate: typing.Optional[datetime] = Field(alias="cq:LastModifiedDate", default=None)
    
    lastmodifieduser: typing.Optional[str] = Field(alias="cq:LastModifiedUser", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    log: typing.Optional[str] = Field(alias="cq:Log", default=None)
    
    logformat: typing.Optional[str] = Field(alias="cq:LogFormat", default=None)
    
    oslc_cm__status: typing.Optional[str] = Field(alias="oslc_cm:status", default=None)
    
    outputfile: typing.Optional[str] = Field(alias="cq:OutputFile", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    reason: typing.Optional[str] = Field(alias="cq:Reason", default=None)
    
    recordscript: typing.Optional[str] = Field(alias="cq:RecordScript", default=None)
    
    recordtype: typing.Optional[str] = Field(alias="cq:RecordType", default=None)
    
    rulesconfig: typing.Optional[str] = Field(alias="cq:RulesConfig", default=None)
    
    timestamps: typing.Optional[str] = Field(alias="cq:TimeStamps", default=None)
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    username: typing.Optional[str] = Field(alias="cq:UserName", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16783914"

    @classmethod
    def shape_name(cls) -> str:
        return "RQ1_Logging"

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
    


class Rq1_LoggingProperty(Enum):
    actionname = "cq:ActionName"
    afilename = "cq:AFileName"
    asamdebug = "cq:ASAMDebug"
    beginsection = "cq:BeginSection"
    cq__Status = "cq:Status"
    currentfiles = "cq:CurrentFiles"
    dbid = "cq:dbid"
    dcterms__type = "dcterms:type"
    debuginstruction = "cq:DebugInstruction"
    debuglevel = "cq:DebugLevel"
    error = "cq:Error"
    fieldname = "cq:FieldName"
    filecontent = "cq:FileContent"
    folder = "cq:Folder"
    hasacl = "cq:hasACL"
    history = "cq:history"
    hook = "cq:Hook"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    instructiontype = "cq:InstructionType"
    is_active = "cq:is_active"
    lastmodifieddate = "cq:LastModifiedDate"
    lastmodifieduser = "cq:LastModifiedUser"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    log = "cq:Log"
    logformat = "cq:LogFormat"
    oslc_cm__status = "oslc_cm:status"
    outputfile = "cq:OutputFile"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    reason = "cq:Reason"
    recordscript = "cq:RecordScript"
    recordtype = "cq:RecordType"
    rulesconfig = "cq:RulesConfig"
    timestamps = "cq:TimeStamps"
    title = "dcterms:title"
    username = "cq:UserName"
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


def create_rq1_logging_changeset(
    actionname: typing.Optional[str] = None,
    afilename: typing.Optional[str] = None,
    asamdebug: typing.Optional[str] = None,
    beginsection: typing.Optional[str] = None,
    cq__Status: typing.Optional[str] = None,
    currentfiles: typing.Optional[str] = None,
    dbid: typing.Optional[str] = None,
    debuginstruction: typing.Optional[str] = None,
    debuglevel: typing.Optional[str] = None,
    error: typing.Optional[str] = None,
    fieldname: typing.Optional[str] = None,
    filecontent: typing.Optional[str] = None,
    folder: typing.Optional[str] = None,
    hasacl: typing.Optional[typing.Union[Resource, 'Commercialacl']] = None,
    history: typing.Optional[Resource] = None,
    hook: typing.Optional[str] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    instructiontype: typing.Optional[str] = None,
    is_active: typing.Optional[int] = None,
    lastmodifieddate: typing.Optional[datetime] = None,
    lastmodifieduser: typing.Optional[str] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    log: typing.Optional[str] = None,
    logformat: typing.Optional[str] = None,
    oslc_cm__status: typing.Optional[str] = None,
    outputfile: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    reason: typing.Optional[str] = None,
    recordscript: typing.Optional[str] = None,
    recordtype: typing.Optional[str] = None,
    rulesconfig: typing.Optional[str] = None,
    timestamps: typing.Optional[str] = None,
    title: typing.Optional[str] = None,
    username: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    
) -> Rq1_Logging:
    '''
    Create a Rq1_Logging changeset.
    '''
    fields = {}
    if actionname is not None:
        fields["cq:ActionName"] = actionname 
    if afilename is not None:
        fields["cq:AFileName"] = afilename 
    if asamdebug is not None:
        fields["cq:ASAMDebug"] = asamdebug 
    if beginsection is not None:
        fields["cq:BeginSection"] = beginsection 
    if cq__Status is not None:
        fields["cq:Status"] = cq__Status 
    if currentfiles is not None:
        fields["cq:CurrentFiles"] = currentfiles 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if debuginstruction is not None:
        fields["cq:DebugInstruction"] = debuginstruction 
    if debuglevel is not None:
        fields["cq:DebugLevel"] = debuglevel 
    if error is not None:
        fields["cq:Error"] = error 
    if fieldname is not None:
        fields["cq:FieldName"] = fieldname 
    if filecontent is not None:
        fields["cq:FileContent"] = filecontent 
    if folder is not None:
        fields["cq:Folder"] = folder 
    if hasacl is not None:
        fields["cq:hasACL"] = hasacl 
    if history is not None:
        fields["cq:history"] = history 
    if hook is not None:
        fields["cq:Hook"] = hook 
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
    if log is not None:
        fields["cq:Log"] = log 
    if logformat is not None:
        fields["cq:LogFormat"] = logformat 
    if oslc_cm__status is not None:
        fields["oslc_cm:status"] = oslc_cm__status 
    if outputfile is not None:
        fields["cq:OutputFile"] = outputfile 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if reason is not None:
        fields["cq:Reason"] = reason 
    if recordscript is not None:
        fields["cq:RecordScript"] = recordscript 
    if recordtype is not None:
        fields["cq:RecordType"] = recordtype 
    if rulesconfig is not None:
        fields["cq:RulesConfig"] = rulesconfig 
    if timestamps is not None:
        fields["cq:TimeStamps"] = timestamps 
    if title is not None:
        fields["dcterms:title"] = title 
    if username is not None:
        fields["cq:UserName"] = username 
    if version is not None:
        fields["cq:version"] = version 
    

    fields["dcterms:type"] = "RQ1_Logging"
    return Rq1_Logging(**fields)