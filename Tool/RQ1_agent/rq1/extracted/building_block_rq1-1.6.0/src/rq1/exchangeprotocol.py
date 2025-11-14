
from .base import Resource, QueryStatement, BaseRecord
from pydantic import ConfigDict, Field, field_validator
import typing
from datetime import datetime, timezone
from enum import Enum
import json


from typing import TYPE_CHECKING

if TYPE_CHECKING:

    from .ratl_replicas import Ratl_Replicas

    from .historylog import Historylog

    from .project import Project

    from .users import Users



class Exchangeprotocol(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    adminlog: typing.Optional[str] = Field(alias="cq:AdminLog", default=None)
    
    belongstoproject: typing.Optional[typing.Union[Resource, 'Project']] = Field(alias="cq:belongsToProject", default=None)
    
    cq__Status: typing.Optional[str] = Field(alias="cq:Status", default=None)
    
    cq__Type: typing.Optional[str] = Field(alias="cq:Type", default=None)
    
    creationcontext: typing.Optional[str] = Field(alias="cq:CreationContext", default=None)
    
    creationmode: typing.Optional[str] = Field(alias="cq:CreationMode", default=None)
    
    creator: typing.Optional[Resource] = Field(alias="dcterms:creator", default=None)
    
    date_time: typing.Optional[datetime] = Field(alias="cq:Date_Time", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    exchangedcommercialfiles: typing.Optional[Resource] = Field(alias="cq:ExchangedCommercialFiles", default=None)
    
    exchangedfiles: typing.Optional[Resource] = Field(alias="cq:ExchangedFiles", default=None)
    
    files: typing.Optional[str] = Field(alias="cq:Files", default=None)
    
    format: typing.Optional[str] = Field(alias="cq:Format", default=None)
    
    hashistorylogs: typing.List[typing.Union[Resource, 'Historylog']] = Field(alias="cq:hasHistoryLogs", default_factory=lambda: [])
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    historylog: typing.Optional[str] = Field(alias="cq:HistoryLog", default=None)
    
    id: typing.Optional[str] = Field(alias="cq:id", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    linkedprojects: typing.Optional[str] = Field(alias="cq:LinkedProjects", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    log: typing.Optional[str] = Field(alias="cq:Log", default=None)
    
    name: typing.Optional[str] = Field(alias="cq:Name", default=None)
    
    operationcontext: typing.Optional[str] = Field(alias="cq:OperationContext", default=None)
    
    operationmode: typing.Optional[str] = Field(alias="cq:OperationMode", default=None)
    
    oslc_cm__status: typing.Optional[str] = Field(alias="oslc_cm:status", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    shorttitle: typing.Optional[str] = Field(alias="oslc:shortTitle", default=None)
    
    submitdate: typing.Optional[datetime] = Field(alias="cq:SubmitDate", default=None)
    
    submitter: typing.Optional[str] = Field(alias="cq:Submitter", default=None)
    
    targetprojects: typing.List[typing.Union[Resource, 'Project']] = Field(alias="cq:targetProjects", default_factory=lambda: [])
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    tld: typing.Optional[str] = Field(alias="cq:TLD", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16777242"

    @classmethod
    def shape_name(cls) -> str:
        return "ExchangeProtocol"

    @field_validator('*', mode="before")
    @classmethod
    def empty_str_to_none(cls, v):
        if v == '':
            return None
        return v

    
    @field_validator('instanceshape', 'history', 'exchangedcommercialfiles', 'exchangedfiles', mode="before")
    @classmethod
    def list_to_resource(cls, v):
        if isinstance(v, list):
            return v[0]
        return v
    


class ExchangeprotocolProperty(Enum):
    adminlog = "cq:AdminLog"
    belongstoproject = "cq:belongsToProject"
    cq__Status = "cq:Status"
    cq__Type = "cq:Type"
    creationcontext = "cq:CreationContext"
    creationmode = "cq:CreationMode"
    creator = "dcterms:creator"
    date_time = "cq:Date_Time"
    dbid = "cq:dbid"
    dcterms__type = "dcterms:type"
    exchangedcommercialfiles = "cq:ExchangedCommercialFiles"
    exchangedfiles = "cq:ExchangedFiles"
    files = "cq:Files"
    format = "cq:Format"
    hashistorylogs = "cq:hasHistoryLogs"
    history = "cq:history"
    historylog = "cq:HistoryLog"
    id = "cq:id"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    is_active = "cq:is_active"
    linkedprojects = "cq:LinkedProjects"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    log = "cq:Log"
    name = "cq:Name"
    operationcontext = "cq:OperationContext"
    operationmode = "cq:OperationMode"
    oslc_cm__status = "oslc_cm:status"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    shorttitle = "oslc:shortTitle"
    submitdate = "cq:SubmitDate"
    submitter = "cq:Submitter"
    targetprojects = "cq:targetProjects"
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


def create_exchangeprotocol_changeset(
    adminlog: typing.Optional[str] = None,
    belongstoproject: typing.Optional[typing.Union[Resource, 'Project']] = None,
    cq__Status: typing.Optional[str] = None,
    cq__Type: typing.Optional[str] = None,
    creationcontext: typing.Optional[str] = None,
    creationmode: typing.Optional[str] = None,
    creator: typing.Optional[Resource] = None,
    date_time: typing.Optional[datetime] = None,
    dbid: typing.Optional[str] = None,
    exchangedcommercialfiles: typing.Optional[Resource] = None,
    exchangedfiles: typing.Optional[Resource] = None,
    files: typing.Optional[str] = None,
    format: typing.Optional[str] = None,
    hashistorylogs: typing.Optional[typing.List[typing.Union[Resource, 'Historylog']]] = None,
    history: typing.Optional[Resource] = None,
    historylog: typing.Optional[str] = None,
    id: typing.Optional[str] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    is_active: typing.Optional[int] = None,
    linkedprojects: typing.Optional[str] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    log: typing.Optional[str] = None,
    name: typing.Optional[str] = None,
    operationcontext: typing.Optional[str] = None,
    operationmode: typing.Optional[str] = None,
    oslc_cm__status: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    shorttitle: typing.Optional[str] = None,
    submitdate: typing.Optional[datetime] = None,
    submitter: typing.Optional[str] = None,
    targetprojects: typing.Optional[typing.List[typing.Union[Resource, 'Project']]] = None,
    title: typing.Optional[str] = None,
    tld: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    
) -> Exchangeprotocol:
    '''
    Create a Exchangeprotocol changeset.
    '''
    fields = {}
    if adminlog is not None:
        fields["cq:AdminLog"] = adminlog 
    if belongstoproject is not None:
        fields["cq:belongsToProject"] = belongstoproject 
    if cq__Status is not None:
        fields["cq:Status"] = cq__Status 
    if cq__Type is not None:
        fields["cq:Type"] = cq__Type 
    if creationcontext is not None:
        fields["cq:CreationContext"] = creationcontext 
    if creationmode is not None:
        fields["cq:CreationMode"] = creationmode 
    if creator is not None:
        fields["dcterms:creator"] = creator 
    if date_time is not None:
        fields["cq:Date_Time"] = date_time 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if exchangedcommercialfiles is not None:
        fields["cq:ExchangedCommercialFiles"] = exchangedcommercialfiles 
    if exchangedfiles is not None:
        fields["cq:ExchangedFiles"] = exchangedfiles 
    if files is not None:
        fields["cq:Files"] = files 
    if format is not None:
        fields["cq:Format"] = format 
    if hashistorylogs is not None:
        fields["cq:hasHistoryLogs"] = hashistorylogs 
    if history is not None:
        fields["cq:history"] = history 
    if historylog is not None:
        fields["cq:HistoryLog"] = historylog 
    if id is not None:
        fields["cq:id"] = id 
    if identifier is not None:
        fields["dcterms:identifier"] = identifier 
    if instanceshape is not None:
        fields["oslc:instanceShape"] = instanceshape 
    if is_active is not None:
        fields["cq:is_active"] = is_active 
    if linkedprojects is not None:
        fields["cq:LinkedProjects"] = linkedprojects 
    if lock_version is not None:
        fields["cq:lock_version"] = lock_version 
    if locked_by is not None:
        fields["cq:locked_by"] = locked_by 
    if log is not None:
        fields["cq:Log"] = log 
    if name is not None:
        fields["cq:Name"] = name 
    if operationcontext is not None:
        fields["cq:OperationContext"] = operationcontext 
    if operationmode is not None:
        fields["cq:OperationMode"] = operationmode 
    if oslc_cm__status is not None:
        fields["oslc_cm:status"] = oslc_cm__status 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if shorttitle is not None:
        fields["oslc:shortTitle"] = shorttitle 
    if submitdate is not None:
        fields["cq:SubmitDate"] = submitdate 
    if submitter is not None:
        fields["cq:Submitter"] = submitter 
    if targetprojects is not None:
        fields["cq:targetProjects"] = targetprojects 
    if title is not None:
        fields["dcterms:title"] = title 
    if tld is not None:
        fields["cq:TLD"] = tld 
    if version is not None:
        fields["cq:version"] = version 
    

    fields["dcterms:type"] = "ExchangeProtocol"
    return Exchangeprotocol(**fields)