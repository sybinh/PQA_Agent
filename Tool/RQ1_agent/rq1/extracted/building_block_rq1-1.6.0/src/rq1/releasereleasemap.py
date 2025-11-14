
from .base import Resource, QueryStatement, BaseRecord
from pydantic import ConfigDict, Field, field_validator
import typing
from datetime import datetime, timezone
from enum import Enum
import json


from typing import TYPE_CHECKING

if TYPE_CHECKING:

    from .historylog import Historylog

    from .externallink import Externallink

    from .users import Users

    from .background import Background

    from .ratl_replicas import Ratl_Replicas

    from .release import Release



class Releasereleasemap(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    accountnumbers: typing.Optional[str] = Field(alias="cq:AccountNumbers", default=None)
    
    assignee: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:Assignee", default=None)
    
    assigneefilter: typing.Optional[str] = Field(alias="cq:AssigneeFilter", default=None)
    
    assigneesearch: typing.Optional[str] = Field(alias="cq:AssigneeSearch", default=None)
    
    changestoconfiguration: typing.Optional[str] = Field(alias="cq:ChangesToConfiguration", default=None)
    
    changestoissues: typing.Optional[str] = Field(alias="cq:ChangesToIssues", default=None)
    
    changestopartlist: typing.Optional[str] = Field(alias="cq:ChangesToPartlist", default=None)
    
    contributiontoderivatives: typing.Optional[str] = Field(alias="cq:ContributionToDerivatives", default=None)
    
    cq__Description: typing.Optional[str] = Field(alias="cq:Description", default=None)
    
    creationmode: typing.Optional[str] = Field(alias="cq:CreationMode", default=None)
    
    creator: typing.Optional[Resource] = Field(alias="dcterms:creator", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__description: typing.Optional[str] = Field(alias="dcterms:description", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    hasbackground: typing.Optional[typing.Union[Resource, 'Background']] = Field(alias="cq:hasBackground", default=None)
    
    hasexternallinks: typing.List[typing.Union[Resource, 'Externallink']] = Field(alias="cq:hasExternalLinks", default_factory=lambda: [])
    
    hashistorylogs: typing.List[typing.Union[Resource, 'Historylog']] = Field(alias="cq:hasHistoryLogs", default_factory=lambda: [])
    
    hasmappedchildfilter: typing.Optional[str] = Field(alias="cq:hasMappedChildFilter", default=None)
    
    hasmappedchildrelease: typing.Optional[typing.Union[Resource, 'Release']] = Field(alias="cq:hasMappedChildRelease", default=None)
    
    hasmappedchildsearch: typing.Optional[str] = Field(alias="cq:hasMappedChildSearch", default=None)
    
    hasmappedparentfilter: typing.Optional[str] = Field(alias="cq:hasMappedParentFilter", default=None)
    
    hasmappedparentrelease: typing.Optional[typing.Union[Resource, 'Release']] = Field(alias="cq:hasMappedParentRelease", default=None)
    
    hasmappedparentsearch: typing.Optional[str] = Field(alias="cq:hasMappedParentSearch", default=None)
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    history_log: typing.Optional[str] = Field(alias="cq:History_Log", default=None)
    
    id: typing.Optional[str] = Field(alias="cq:id", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    integrationaction: typing.Optional[str] = Field(alias="cq:IntegrationAction", default=None)
    
    integrationstep: typing.Optional[str] = Field(alias="cq:IntegrationStep", default=None)
    
    internalcomment: typing.Optional[str] = Field(alias="cq:InternalComment", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    ispilot: typing.Optional[str] = Field(alias="cq:isPilot", default=None)
    
    lastlifecyclestate: typing.Optional[str] = Field(alias="cq:LastLifeCycleState", default=None)
    
    lastmodifieddate: typing.Optional[datetime] = Field(alias="cq:LastModifiedDate", default=None)
    
    lifecyclestate: typing.Optional[str] = Field(alias="cq:LifeCycleState", default=None)
    
    lifecyclestatecomment: typing.Optional[str] = Field(alias="cq:LifeCycleStateComment", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    milestones: typing.Optional[str] = Field(alias="cq:Milestones", default=None)
    
    note_entry: typing.Optional[str] = Field(alias="cq:Note_Entry", default=None)
    
    notes_log: typing.Optional[str] = Field(alias="cq:Notes_Log", default=None)
    
    operationcontext: typing.Optional[str] = Field(alias="cq:OperationContext", default=None)
    
    operationmode: typing.Optional[str] = Field(alias="cq:OperationMode", default=None)
    
    oslccommand: typing.Optional[str] = Field(alias="cq:oslcCommand", default=None)
    
    ratl_forminfo: typing.Optional[str] = Field(alias="cq:ratl_forminfo", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    requesteddeliverydate: typing.Optional[datetime] = Field(alias="cq:RequestedDeliveryDate", default=None)
    
    requestedimplementfreeze: typing.Optional[datetime] = Field(alias="cq:RequestedImplementFreeze", default=None)
    
    requestedmaturitylevel: typing.Optional[str] = Field(alias="cq:RequestedMaturityLevel", default=None)
    
    selectassignee: typing.Optional[str] = Field(alias="cq:SelectAssignee", default=None)
    
    selectionofderivatives: typing.Optional[str] = Field(alias="cq:SelectionOfDerivatives", default=None)
    
    shorttitle: typing.Optional[str] = Field(alias="oslc:shortTitle", default=None)
    
    subject: typing.List[str] = Field(alias="dcterms:subject", default_factory=lambda: [])
    
    submitdate: typing.Optional[datetime] = Field(alias="cq:SubmitDate", default=None)
    
    submitter: typing.Optional[str] = Field(alias="cq:Submitter", default=None)
    
    suppressvalidation: typing.Optional[str] = Field(alias="cq:SuppressValidation", default=None)
    
    suppressvalidationcomment: typing.Optional[str] = Field(alias="cq:SuppressValidationComment", default=None)
    
    tags: typing.Optional[str] = Field(alias="cq:Tags", default=None)
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    tld: typing.Optional[str] = Field(alias="cq:TLD", default=None)
    
    toolversion: typing.Optional[str] = Field(alias="cq:ToolVersion", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    
    workflowstatus: typing.Optional[str] = Field(alias="cq:WorkflowStatus", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16777233"

    @classmethod
    def shape_name(cls) -> str:
        return "Releasereleasemap"

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
    


class ReleasereleasemapProperty(Enum):
    accountnumbers = "cq:AccountNumbers"
    assignee = "cq:Assignee"
    assigneefilter = "cq:AssigneeFilter"
    assigneesearch = "cq:AssigneeSearch"
    changestoconfiguration = "cq:ChangesToConfiguration"
    changestoissues = "cq:ChangesToIssues"
    changestopartlist = "cq:ChangesToPartlist"
    contributiontoderivatives = "cq:ContributionToDerivatives"
    cq__Description = "cq:Description"
    creationmode = "cq:CreationMode"
    creator = "dcterms:creator"
    dbid = "cq:dbid"
    dcterms__description = "dcterms:description"
    dcterms__type = "dcterms:type"
    hasbackground = "cq:hasBackground"
    hasexternallinks = "cq:hasExternalLinks"
    hashistorylogs = "cq:hasHistoryLogs"
    hasmappedchildfilter = "cq:hasMappedChildFilter"
    hasmappedchildrelease = "cq:hasMappedChildRelease"
    hasmappedchildsearch = "cq:hasMappedChildSearch"
    hasmappedparentfilter = "cq:hasMappedParentFilter"
    hasmappedparentrelease = "cq:hasMappedParentRelease"
    hasmappedparentsearch = "cq:hasMappedParentSearch"
    history = "cq:history"
    history_log = "cq:History_Log"
    id = "cq:id"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    integrationaction = "cq:IntegrationAction"
    integrationstep = "cq:IntegrationStep"
    internalcomment = "cq:InternalComment"
    is_active = "cq:is_active"
    ispilot = "cq:isPilot"
    lastlifecyclestate = "cq:LastLifeCycleState"
    lastmodifieddate = "cq:LastModifiedDate"
    lifecyclestate = "cq:LifeCycleState"
    lifecyclestatecomment = "cq:LifeCycleStateComment"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    milestones = "cq:Milestones"
    note_entry = "cq:Note_Entry"
    notes_log = "cq:Notes_Log"
    operationcontext = "cq:OperationContext"
    operationmode = "cq:OperationMode"
    oslccommand = "cq:oslcCommand"
    ratl_forminfo = "cq:ratl_forminfo"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    requesteddeliverydate = "cq:RequestedDeliveryDate"
    requestedimplementfreeze = "cq:RequestedImplementFreeze"
    requestedmaturitylevel = "cq:RequestedMaturityLevel"
    selectassignee = "cq:SelectAssignee"
    selectionofderivatives = "cq:SelectionOfDerivatives"
    shorttitle = "oslc:shortTitle"
    subject = "dcterms:subject"
    submitdate = "cq:SubmitDate"
    submitter = "cq:Submitter"
    suppressvalidation = "cq:SuppressValidation"
    suppressvalidationcomment = "cq:SuppressValidationComment"
    tags = "cq:Tags"
    title = "dcterms:title"
    tld = "cq:TLD"
    toolversion = "cq:ToolVersion"
    version = "cq:version"
    workflowstatus = "cq:WorkflowStatus"
    

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


def create_releasereleasemap_changeset(
    accountnumbers: typing.Optional[str] = None,
    assignee: typing.Optional[typing.Union[Resource, 'Users']] = None,
    assigneefilter: typing.Optional[str] = None,
    assigneesearch: typing.Optional[str] = None,
    changestoconfiguration: typing.Optional[str] = None,
    changestoissues: typing.Optional[str] = None,
    changestopartlist: typing.Optional[str] = None,
    contributiontoderivatives: typing.Optional[str] = None,
    cq__Description: typing.Optional[str] = None,
    creationmode: typing.Optional[str] = None,
    creator: typing.Optional[Resource] = None,
    dbid: typing.Optional[str] = None,
    dcterms__description: typing.Optional[str] = None,
    hasbackground: typing.Optional[typing.Union[Resource, 'Background']] = None,
    hasexternallinks: typing.Optional[typing.List[typing.Union[Resource, 'Externallink']]] = None,
    hashistorylogs: typing.Optional[typing.List[typing.Union[Resource, 'Historylog']]] = None,
    hasmappedchildfilter: typing.Optional[str] = None,
    hasmappedchildrelease: typing.Optional[typing.Union[Resource, 'Release']] = None,
    hasmappedchildsearch: typing.Optional[str] = None,
    hasmappedparentfilter: typing.Optional[str] = None,
    hasmappedparentrelease: typing.Optional[typing.Union[Resource, 'Release']] = None,
    hasmappedparentsearch: typing.Optional[str] = None,
    history: typing.Optional[Resource] = None,
    history_log: typing.Optional[str] = None,
    id: typing.Optional[str] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    integrationaction: typing.Optional[str] = None,
    integrationstep: typing.Optional[str] = None,
    internalcomment: typing.Optional[str] = None,
    is_active: typing.Optional[int] = None,
    ispilot: typing.Optional[str] = None,
    lastlifecyclestate: typing.Optional[str] = None,
    lastmodifieddate: typing.Optional[datetime] = None,
    lifecyclestate: typing.Optional[str] = None,
    lifecyclestatecomment: typing.Optional[str] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    milestones: typing.Optional[str] = None,
    note_entry: typing.Optional[str] = None,
    notes_log: typing.Optional[str] = None,
    operationcontext: typing.Optional[str] = None,
    operationmode: typing.Optional[str] = None,
    oslccommand: typing.Optional[str] = None,
    ratl_forminfo: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    requesteddeliverydate: typing.Optional[datetime] = None,
    requestedimplementfreeze: typing.Optional[datetime] = None,
    requestedmaturitylevel: typing.Optional[str] = None,
    selectassignee: typing.Optional[str] = None,
    selectionofderivatives: typing.Optional[str] = None,
    shorttitle: typing.Optional[str] = None,
    subject: typing.Optional[typing.List[str]] = None,
    submitdate: typing.Optional[datetime] = None,
    submitter: typing.Optional[str] = None,
    suppressvalidation: typing.Optional[str] = None,
    suppressvalidationcomment: typing.Optional[str] = None,
    tags: typing.Optional[str] = None,
    title: typing.Optional[str] = None,
    tld: typing.Optional[str] = None,
    toolversion: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    workflowstatus: typing.Optional[str] = None,
    
) -> Releasereleasemap:
    '''
    Create a Releasereleasemap changeset.
    '''
    fields = {}
    if accountnumbers is not None:
        fields["cq:AccountNumbers"] = accountnumbers 
    if assignee is not None:
        fields["cq:Assignee"] = assignee 
    if assigneefilter is not None:
        fields["cq:AssigneeFilter"] = assigneefilter 
    if assigneesearch is not None:
        fields["cq:AssigneeSearch"] = assigneesearch 
    if changestoconfiguration is not None:
        fields["cq:ChangesToConfiguration"] = changestoconfiguration 
    if changestoissues is not None:
        fields["cq:ChangesToIssues"] = changestoissues 
    if changestopartlist is not None:
        fields["cq:ChangesToPartlist"] = changestopartlist 
    if contributiontoderivatives is not None:
        fields["cq:ContributionToDerivatives"] = contributiontoderivatives 
    if cq__Description is not None:
        fields["cq:Description"] = cq__Description 
    if creationmode is not None:
        fields["cq:CreationMode"] = creationmode 
    if creator is not None:
        fields["dcterms:creator"] = creator 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if dcterms__description is not None:
        fields["dcterms:description"] = dcterms__description 
    if hasbackground is not None:
        fields["cq:hasBackground"] = hasbackground 
    if hasexternallinks is not None:
        fields["cq:hasExternalLinks"] = hasexternallinks 
    if hashistorylogs is not None:
        fields["cq:hasHistoryLogs"] = hashistorylogs 
    if hasmappedchildfilter is not None:
        fields["cq:hasMappedChildFilter"] = hasmappedchildfilter 
    if hasmappedchildrelease is not None:
        fields["cq:hasMappedChildRelease"] = hasmappedchildrelease 
    if hasmappedchildsearch is not None:
        fields["cq:hasMappedChildSearch"] = hasmappedchildsearch 
    if hasmappedparentfilter is not None:
        fields["cq:hasMappedParentFilter"] = hasmappedparentfilter 
    if hasmappedparentrelease is not None:
        fields["cq:hasMappedParentRelease"] = hasmappedparentrelease 
    if hasmappedparentsearch is not None:
        fields["cq:hasMappedParentSearch"] = hasmappedparentsearch 
    if history is not None:
        fields["cq:history"] = history 
    if history_log is not None:
        fields["cq:History_Log"] = history_log 
    if id is not None:
        fields["cq:id"] = id 
    if identifier is not None:
        fields["dcterms:identifier"] = identifier 
    if instanceshape is not None:
        fields["oslc:instanceShape"] = instanceshape 
    if integrationaction is not None:
        fields["cq:IntegrationAction"] = integrationaction 
    if integrationstep is not None:
        fields["cq:IntegrationStep"] = integrationstep 
    if internalcomment is not None:
        fields["cq:InternalComment"] = internalcomment 
    if is_active is not None:
        fields["cq:is_active"] = is_active 
    if ispilot is not None:
        fields["cq:isPilot"] = ispilot 
    if lastlifecyclestate is not None:
        fields["cq:LastLifeCycleState"] = lastlifecyclestate 
    if lastmodifieddate is not None:
        fields["cq:LastModifiedDate"] = lastmodifieddate 
    if lifecyclestate is not None:
        fields["cq:LifeCycleState"] = lifecyclestate 
    if lifecyclestatecomment is not None:
        fields["cq:LifeCycleStateComment"] = lifecyclestatecomment 
    if lock_version is not None:
        fields["cq:lock_version"] = lock_version 
    if locked_by is not None:
        fields["cq:locked_by"] = locked_by 
    if milestones is not None:
        fields["cq:Milestones"] = milestones 
    if note_entry is not None:
        fields["cq:Note_Entry"] = note_entry 
    if notes_log is not None:
        fields["cq:Notes_Log"] = notes_log 
    if operationcontext is not None:
        fields["cq:OperationContext"] = operationcontext 
    if operationmode is not None:
        fields["cq:OperationMode"] = operationmode 
    if oslccommand is not None:
        fields["cq:oslcCommand"] = oslccommand 
    if ratl_forminfo is not None:
        fields["cq:ratl_forminfo"] = ratl_forminfo 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if requesteddeliverydate is not None:
        fields["cq:RequestedDeliveryDate"] = requesteddeliverydate 
    if requestedimplementfreeze is not None:
        fields["cq:RequestedImplementFreeze"] = requestedimplementfreeze 
    if requestedmaturitylevel is not None:
        fields["cq:RequestedMaturityLevel"] = requestedmaturitylevel 
    if selectassignee is not None:
        fields["cq:SelectAssignee"] = selectassignee 
    if selectionofderivatives is not None:
        fields["cq:SelectionOfDerivatives"] = selectionofderivatives 
    if shorttitle is not None:
        fields["oslc:shortTitle"] = shorttitle 
    if subject is not None:
        fields["dcterms:subject"] = subject 
    if submitdate is not None:
        fields["cq:SubmitDate"] = submitdate 
    if submitter is not None:
        fields["cq:Submitter"] = submitter 
    if suppressvalidation is not None:
        fields["cq:SuppressValidation"] = suppressvalidation 
    if suppressvalidationcomment is not None:
        fields["cq:SuppressValidationComment"] = suppressvalidationcomment 
    if tags is not None:
        fields["cq:Tags"] = tags 
    if title is not None:
        fields["dcterms:title"] = title 
    if tld is not None:
        fields["cq:TLD"] = tld 
    if toolversion is not None:
        fields["cq:ToolVersion"] = toolversion 
    if version is not None:
        fields["cq:version"] = version 
    if workflowstatus is not None:
        fields["cq:WorkflowStatus"] = workflowstatus 
    

    fields["dcterms:type"] = "Releasereleasemap"
    return Releasereleasemap(**fields)