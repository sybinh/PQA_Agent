
from .base import Resource, QueryStatement, BaseRecord
from pydantic import ConfigDict, Field, field_validator
import typing
from datetime import datetime, timezone
from enum import Enum
import json


from typing import TYPE_CHECKING

if TYPE_CHECKING:

    from .historylog import Historylog

    from .groups import Groups

    from .users import Users

    from .commercialacl import Commercialacl

    from .issuereleasemap import Issuereleasemap

    from .issue import Issue

    from .ratl_replicas import Ratl_Replicas



class Commercial(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    belongstoissue: typing.List[typing.Union[Resource, 'Issue']] = Field(alias="cq:belongsToIssue", default_factory=lambda: [])
    
    belongstoissuereleasemap: typing.List[typing.Union[Resource, 'Issuereleasemap']] = Field(alias="cq:belongsToIssuereleasemap", default_factory=lambda: [])
    
    commercialamount: typing.Optional[str] = Field(alias="cq:CommercialAmount", default=None)
    
    commercialamountconfirmed: typing.Optional[typing.Literal["No", "Yes"]] = Field(alias="cq:CommercialAmountConfirmed", default=None)
    
    commercialamountdetails: typing.Optional[str] = Field(alias="cq:CommercialAmountDetails", default=None)
    
    commercialamountunit: typing.Optional[str] = Field(alias="cq:CommercialAmountUnit", default=None)
    
    commercialassignee: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:CommercialAssignee", default=None)
    
    commercialattachments: typing.Optional[Resource] = Field(alias="cq:CommercialAttachments", default=None)
    
    commercialcomment: typing.Optional[str] = Field(alias="cq:CommercialComment", default=None)
    
    commercialconversation: typing.Optional[str] = Field(alias="cq:CommercialConversation", default=None)
    
    commercialorigsolutionacc: typing.Optional[str] = Field(alias="cq:CommercialOrigSolutionAcc", default=None)
    
    commercialtags: typing.Optional[str] = Field(alias="cq:CommercialTags", default=None)
    
    cq__Title: typing.Optional[str] = Field(alias="cq:Title", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    hasacl: typing.Optional[typing.Union[Resource, 'Commercialacl']] = Field(alias="cq:hasACL", default=None)
    
    hashistorylogs: typing.List[typing.Union[Resource, 'Historylog']] = Field(alias="cq:hasHistoryLogs", default_factory=lambda: [])
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    history_log: typing.Optional[str] = Field(alias="cq:History_Log", default=None)
    
    historylog: typing.Optional[str] = Field(alias="cq:HistoryLog", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    operationcontext: typing.Optional[str] = Field(alias="cq:OperationContext", default=None)
    
    operationmode: typing.Optional[str] = Field(alias="cq:OperationMode", default=None)
    
    ratl_context_groups: typing.List[typing.Union[Resource, 'Groups']] = Field(alias="cq:ratl_context_groups", default_factory=lambda: [])
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    shorttitle: typing.Optional[str] = Field(alias="oslc:shortTitle", default=None)
    
    tld: typing.Optional[str] = Field(alias="cq:TLD", default=None)
    
    toolversion: typing.Optional[str] = Field(alias="cq:ToolVersion", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16777245"

    @classmethod
    def shape_name(cls) -> str:
        return "Commercial"

    @field_validator('*', mode="before")
    @classmethod
    def empty_str_to_none(cls, v):
        if v == '':
            return None
        return v

    
    @field_validator('instanceshape', 'history', 'commercialattachments', mode="before")
    @classmethod
    def list_to_resource(cls, v):
        if isinstance(v, list):
            return v[0]
        return v
    


class CommercialProperty(Enum):
    belongstoissue = "cq:belongsToIssue"
    belongstoissuereleasemap = "cq:belongsToIssuereleasemap"
    commercialamount = "cq:CommercialAmount"
    commercialamountconfirmed = "cq:CommercialAmountConfirmed"
    commercialamountdetails = "cq:CommercialAmountDetails"
    commercialamountunit = "cq:CommercialAmountUnit"
    commercialassignee = "cq:CommercialAssignee"
    commercialattachments = "cq:CommercialAttachments"
    commercialcomment = "cq:CommercialComment"
    commercialconversation = "cq:CommercialConversation"
    commercialorigsolutionacc = "cq:CommercialOrigSolutionAcc"
    commercialtags = "cq:CommercialTags"
    cq__Title = "cq:Title"
    dbid = "cq:dbid"
    dcterms__title = "dcterms:title"
    dcterms__type = "dcterms:type"
    hasacl = "cq:hasACL"
    hashistorylogs = "cq:hasHistoryLogs"
    history = "cq:history"
    history_log = "cq:History_Log"
    historylog = "cq:HistoryLog"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    is_active = "cq:is_active"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    operationcontext = "cq:OperationContext"
    operationmode = "cq:OperationMode"
    ratl_context_groups = "cq:ratl_context_groups"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    shorttitle = "oslc:shortTitle"
    tld = "cq:TLD"
    toolversion = "cq:ToolVersion"
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


def create_commercial_changeset(
    belongstoissue: typing.Optional[typing.List[typing.Union[Resource, 'Issue']]] = None,
    belongstoissuereleasemap: typing.Optional[typing.List[typing.Union[Resource, 'Issuereleasemap']]] = None,
    commercialamount: typing.Optional[str] = None,
    commercialamountconfirmed: typing.Optional[typing.Literal["No", "Yes"]] = None,
    commercialamountdetails: typing.Optional[str] = None,
    commercialamountunit: typing.Optional[str] = None,
    commercialassignee: typing.Optional[typing.Union[Resource, 'Users']] = None,
    commercialattachments: typing.Optional[Resource] = None,
    commercialcomment: typing.Optional[str] = None,
    commercialconversation: typing.Optional[str] = None,
    commercialorigsolutionacc: typing.Optional[str] = None,
    commercialtags: typing.Optional[str] = None,
    cq__Title: typing.Optional[str] = None,
    dbid: typing.Optional[str] = None,
    dcterms__title: typing.Optional[str] = None,
    hasacl: typing.Optional[typing.Union[Resource, 'Commercialacl']] = None,
    hashistorylogs: typing.Optional[typing.List[typing.Union[Resource, 'Historylog']]] = None,
    history: typing.Optional[Resource] = None,
    history_log: typing.Optional[str] = None,
    historylog: typing.Optional[str] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    is_active: typing.Optional[int] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    operationcontext: typing.Optional[str] = None,
    operationmode: typing.Optional[str] = None,
    ratl_context_groups: typing.Optional[typing.List[typing.Union[Resource, 'Groups']]] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    shorttitle: typing.Optional[str] = None,
    tld: typing.Optional[str] = None,
    toolversion: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    
) -> Commercial:
    '''
    Create a Commercial changeset.
    '''
    fields = {}
    if belongstoissue is not None:
        fields["cq:belongsToIssue"] = belongstoissue 
    if belongstoissuereleasemap is not None:
        fields["cq:belongsToIssuereleasemap"] = belongstoissuereleasemap 
    if commercialamount is not None:
        fields["cq:CommercialAmount"] = commercialamount 
    if commercialamountconfirmed is not None:
        fields["cq:CommercialAmountConfirmed"] = commercialamountconfirmed 
    if commercialamountdetails is not None:
        fields["cq:CommercialAmountDetails"] = commercialamountdetails 
    if commercialamountunit is not None:
        fields["cq:CommercialAmountUnit"] = commercialamountunit 
    if commercialassignee is not None:
        fields["cq:CommercialAssignee"] = commercialassignee 
    if commercialattachments is not None:
        fields["cq:CommercialAttachments"] = commercialattachments 
    if commercialcomment is not None:
        fields["cq:CommercialComment"] = commercialcomment 
    if commercialconversation is not None:
        fields["cq:CommercialConversation"] = commercialconversation 
    if commercialorigsolutionacc is not None:
        fields["cq:CommercialOrigSolutionAcc"] = commercialorigsolutionacc 
    if commercialtags is not None:
        fields["cq:CommercialTags"] = commercialtags 
    if cq__Title is not None:
        fields["cq:Title"] = cq__Title 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if dcterms__title is not None:
        fields["dcterms:title"] = dcterms__title 
    if hasacl is not None:
        fields["cq:hasACL"] = hasacl 
    if hashistorylogs is not None:
        fields["cq:hasHistoryLogs"] = hashistorylogs 
    if history is not None:
        fields["cq:history"] = history 
    if history_log is not None:
        fields["cq:History_Log"] = history_log 
    if historylog is not None:
        fields["cq:HistoryLog"] = historylog 
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
    if operationcontext is not None:
        fields["cq:OperationContext"] = operationcontext 
    if operationmode is not None:
        fields["cq:OperationMode"] = operationmode 
    if ratl_context_groups is not None:
        fields["cq:ratl_context_groups"] = ratl_context_groups 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if shorttitle is not None:
        fields["oslc:shortTitle"] = shorttitle 
    if tld is not None:
        fields["cq:TLD"] = tld 
    if toolversion is not None:
        fields["cq:ToolVersion"] = toolversion 
    if version is not None:
        fields["cq:version"] = version 
    

    fields["dcterms:type"] = "Commercial"
    return Commercial(**fields)