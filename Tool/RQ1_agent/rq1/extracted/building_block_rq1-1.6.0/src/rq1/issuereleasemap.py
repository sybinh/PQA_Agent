
from .base import Resource, QueryStatement, BaseRecord
from pydantic import ConfigDict, Field, field_validator
import typing
from datetime import datetime, timezone
from enum import Enum
import json


from typing import TYPE_CHECKING

if TYPE_CHECKING:

    from .commercial import Commercial

    from .historylog import Historylog

    from .externallink import Externallink

    from .attachmentmapping import Attachmentmapping

    from .users import Users

    from .issue import Issue

    from .background import Background

    from .ratl_replicas import Ratl_Replicas

    from .release import Release

    from .contact import Contact



class Issuereleasemap(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    accountnumbers: typing.Optional[str] = Field(alias="cq:AccountNumbers", default=None)
    
    actualconfiguration: typing.Optional[str] = Field(alias="cq:ActualConfiguration", default=None)
    
    assignee: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:Assignee", default=None)
    
    assigneefilter: typing.Optional[str] = Field(alias="cq:AssigneeFilter", default=None)
    
    assigneesearch: typing.Optional[str] = Field(alias="cq:AssigneeSearch", default=None)
    
    changestoconfiguration: typing.Optional[str] = Field(alias="cq:ChangesToConfiguration", default=None)
    
    changestoissues: typing.Optional[str] = Field(alias="cq:ChangesToIssues", default=None)
    
    changestopartlist: typing.Optional[str] = Field(alias="cq:ChangesToPartlist", default=None)
    
    commercialassignee: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:CommercialAssignee", default=None)
    
    commercialassigneefilter: typing.Optional[str] = Field(alias="cq:CommercialAssigneeFilter", default=None)
    
    commercialassigneesearch: typing.Optional[str] = Field(alias="cq:CommercialAssigneeSearch", default=None)
    
    cq__Description: typing.Optional[str] = Field(alias="cq:Description", default=None)
    
    createcommercial: typing.Optional[typing.Literal["No", "Yes"]] = Field(alias="cq:CreateCommercial", default=None)
    
    creationmode: typing.Optional[str] = Field(alias="cq:CreationMode", default=None)
    
    creator: typing.Optional[Resource] = Field(alias="dcterms:creator", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__description: typing.Optional[str] = Field(alias="dcterms:description", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    external_id: typing.Optional[str] = Field(alias="cq:External_ID", default=None)
    
    externalassignee: typing.Optional[typing.Union[Resource, 'Contact']] = Field(alias="cq:ExternalAssignee", default=None)
    
    externalcomment: typing.Optional[str] = Field(alias="cq:ExternalComment", default=None)
    
    externalcommentauthor: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:ExternalCommentAuthor", default=None)
    
    externalconversation: typing.Optional[str] = Field(alias="cq:ExternalConversation", default=None)
    
    externaldescription: typing.Optional[str] = Field(alias="cq:ExternalDescription", default=None)
    
    externalexchangeattach: typing.Optional[str] = Field(alias="cq:ExternalExchangeAttach", default=None)
    
    externalexchangeformat: typing.Optional[str] = Field(alias="cq:ExternalExchangeFormat", default=None)
    
    externalexchangeworkflow: typing.Optional[str] = Field(alias="cq:ExternalExchangeWorkflow", default=None)
    
    externalhistory: typing.Optional[str] = Field(alias="cq:ExternalHistory", default=None)
    
    externalinformation: typing.Optional[str] = Field(alias="cq:ExternalInformation", default=None)
    
    externallastexporteddate: typing.Optional[datetime] = Field(alias="cq:ExternalLastExportedDate", default=None)
    
    externallastimporteddate: typing.Optional[datetime] = Field(alias="cq:ExternalLastImportedDate", default=None)
    
    externalnextstate: typing.Optional[str] = Field(alias="cq:ExternalNextState", default=None)
    
    externalreview: typing.Optional[str] = Field(alias="cq:ExternalReview", default=None)
    
    externalstate: typing.Optional[str] = Field(alias="cq:ExternalState", default=None)
    
    externalstate_parallel1: typing.Optional[str] = Field(alias="cq:ExternalState_Parallel1", default=None)
    
    externalstate_parallel2: typing.Optional[str] = Field(alias="cq:ExternalState_Parallel2", default=None)
    
    externaltags: typing.Optional[str] = Field(alias="cq:ExternalTags", default=None)
    
    externaltitle: typing.Optional[str] = Field(alias="cq:ExternalTitle", default=None)
    
    externalupdateversion: typing.Optional[str] = Field(alias="cq:ExternalUpdateVersion", default=None)
    
    hasattachmentmappings: typing.List[typing.Union[Resource, 'Attachmentmapping']] = Field(alias="cq:hasAttachmentMappings", default_factory=lambda: [])
    
    hasbackground: typing.Optional[typing.Union[Resource, 'Background']] = Field(alias="cq:hasBackground", default=None)
    
    hascommercialdata: typing.Optional[typing.Union[Resource, 'Commercial']] = Field(alias="cq:hasCommercialData", default=None)
    
    hasexternallinks: typing.List[typing.Union[Resource, 'Externallink']] = Field(alias="cq:hasExternalLinks", default_factory=lambda: [])
    
    hashistorylogs: typing.List[typing.Union[Resource, 'Historylog']] = Field(alias="cq:hasHistoryLogs", default_factory=lambda: [])
    
    hasmappedissue: typing.Optional[typing.Union[Resource, 'Issue']] = Field(alias="cq:hasMappedIssue", default=None)
    
    hasmappedissuefilter: typing.Optional[str] = Field(alias="cq:hasMappedIssueFilter", default=None)
    
    hasmappedissuesearch: typing.Optional[str] = Field(alias="cq:hasMappedIssueSearch", default=None)
    
    hasmappedrelease: typing.Optional[typing.Union[Resource, 'Release']] = Field(alias="cq:hasMappedRelease", default=None)
    
    hasmappedreleasefilter: typing.Optional[str] = Field(alias="cq:hasMappedReleaseFilter", default=None)
    
    hasmappedreleasesearch: typing.Optional[str] = Field(alias="cq:hasMappedReleaseSearch", default=None)
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    history_log: typing.Optional[str] = Field(alias="cq:History_Log", default=None)
    
    id: typing.Optional[str] = Field(alias="cq:id", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    impactanalysis: typing.Optional[str] = Field(alias="cq:ImpactAnalysis", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    integrationstep: typing.Optional[str] = Field(alias="cq:IntegrationStep", default=None)
    
    internalcomment: typing.Optional[str] = Field(alias="cq:InternalComment", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    ispilot: typing.Optional[str] = Field(alias="cq:isPilot", default=None)
    
    lastlifecyclestate: typing.Optional[str] = Field(alias="cq:LastLifeCycleState", default=None)
    
    lastminutechange: typing.Optional[str] = Field(alias="cq:LastMinuteChange", default=None)
    
    lastmodifieddate: typing.Optional[datetime] = Field(alias="cq:LastModifiedDate", default=None)
    
    latechange: typing.Optional[str] = Field(alias="cq:LateChange", default=None)
    
    lifecyclestate: typing.Optional[str] = Field(alias="cq:LifeCycleState", default=None)
    
    lifecyclestatecomment: typing.Optional[str] = Field(alias="cq:LifeCycleStateComment", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    mappingtoderivatives: typing.Optional[str] = Field(alias="cq:MappingToDerivatives", default=None)
    
    milestones: typing.Optional[str] = Field(alias="cq:Milestones", default=None)
    
    note_entry: typing.Optional[str] = Field(alias="cq:Note_Entry", default=None)
    
    notes_log: typing.Optional[str] = Field(alias="cq:Notes_Log", default=None)
    
    operationcontext: typing.Optional[str] = Field(alias="cq:OperationContext", default=None)
    
    operationmode: typing.Optional[str] = Field(alias="cq:OperationMode", default=None)
    
    oslccommand: typing.Optional[str] = Field(alias="cq:oslcCommand", default=None)
    
    planningaction: typing.Optional[str] = Field(alias="cq:PlanningAction", default=None)
    
    priority: typing.Optional[str] = Field(alias="cq:Priority", default=None)
    
    prioritycomment: typing.Optional[str] = Field(alias="cq:PriorityComment", default=None)
    
    qualificationmeasure: typing.Optional[str] = Field(alias="cq:QualificationMeasure", default=None)
    
    qualificationstatus: typing.Optional[str] = Field(alias="cq:QualificationStatus", default=None)
    
    ratl_forminfo: typing.Optional[str] = Field(alias="cq:ratl_forminfo", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    requestedconfiguration: typing.Optional[str] = Field(alias="cq:RequestedConfiguration", default=None)
    
    scope: typing.Optional[str] = Field(alias="cq:Scope", default=None)
    
    selectassignee: typing.Optional[str] = Field(alias="cq:SelectAssignee", default=None)
    
    shorttitle: typing.Optional[str] = Field(alias="oslc:shortTitle", default=None)
    
    subject: typing.List[str] = Field(alias="dcterms:subject", default_factory=lambda: [])
    
    submitdate: typing.Optional[datetime] = Field(alias="cq:SubmitDate", default=None)
    
    submitter: typing.Optional[str] = Field(alias="cq:Submitter", default=None)
    
    suppressvalidation: typing.Optional[str] = Field(alias="cq:SuppressValidation", default=None)
    
    suppressvalidationcomment: typing.Optional[str] = Field(alias="cq:SuppressValidationComment", default=None)
    
    swarchitecturedecision: typing.Optional[str] = Field(alias="cq:SwArchitectureDecision", default=None)
    
    tags: typing.Optional[str] = Field(alias="cq:Tags", default=None)
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    tld: typing.Optional[str] = Field(alias="cq:TLD", default=None)
    
    toolversion: typing.Optional[str] = Field(alias="cq:ToolVersion", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    
    workflowstatus: typing.Optional[str] = Field(alias="cq:WorkflowStatus", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16777234"

    @classmethod
    def shape_name(cls) -> str:
        return "Issuereleasemap"

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
    


class IssuereleasemapProperty(Enum):
    accountnumbers = "cq:AccountNumbers"
    actualconfiguration = "cq:ActualConfiguration"
    assignee = "cq:Assignee"
    assigneefilter = "cq:AssigneeFilter"
    assigneesearch = "cq:AssigneeSearch"
    changestoconfiguration = "cq:ChangesToConfiguration"
    changestoissues = "cq:ChangesToIssues"
    changestopartlist = "cq:ChangesToPartlist"
    commercialassignee = "cq:CommercialAssignee"
    commercialassigneefilter = "cq:CommercialAssigneeFilter"
    commercialassigneesearch = "cq:CommercialAssigneeSearch"
    cq__Description = "cq:Description"
    createcommercial = "cq:CreateCommercial"
    creationmode = "cq:CreationMode"
    creator = "dcterms:creator"
    dbid = "cq:dbid"
    dcterms__description = "dcterms:description"
    dcterms__type = "dcterms:type"
    external_id = "cq:External_ID"
    externalassignee = "cq:ExternalAssignee"
    externalcomment = "cq:ExternalComment"
    externalcommentauthor = "cq:ExternalCommentAuthor"
    externalconversation = "cq:ExternalConversation"
    externaldescription = "cq:ExternalDescription"
    externalexchangeattach = "cq:ExternalExchangeAttach"
    externalexchangeformat = "cq:ExternalExchangeFormat"
    externalexchangeworkflow = "cq:ExternalExchangeWorkflow"
    externalhistory = "cq:ExternalHistory"
    externalinformation = "cq:ExternalInformation"
    externallastexporteddate = "cq:ExternalLastExportedDate"
    externallastimporteddate = "cq:ExternalLastImportedDate"
    externalnextstate = "cq:ExternalNextState"
    externalreview = "cq:ExternalReview"
    externalstate = "cq:ExternalState"
    externalstate_parallel1 = "cq:ExternalState_Parallel1"
    externalstate_parallel2 = "cq:ExternalState_Parallel2"
    externaltags = "cq:ExternalTags"
    externaltitle = "cq:ExternalTitle"
    externalupdateversion = "cq:ExternalUpdateVersion"
    hasattachmentmappings = "cq:hasAttachmentMappings"
    hasbackground = "cq:hasBackground"
    hascommercialdata = "cq:hasCommercialData"
    hasexternallinks = "cq:hasExternalLinks"
    hashistorylogs = "cq:hasHistoryLogs"
    hasmappedissue = "cq:hasMappedIssue"
    hasmappedissuefilter = "cq:hasMappedIssueFilter"
    hasmappedissuesearch = "cq:hasMappedIssueSearch"
    hasmappedrelease = "cq:hasMappedRelease"
    hasmappedreleasefilter = "cq:hasMappedReleaseFilter"
    hasmappedreleasesearch = "cq:hasMappedReleaseSearch"
    history = "cq:history"
    history_log = "cq:History_Log"
    id = "cq:id"
    identifier = "dcterms:identifier"
    impactanalysis = "cq:ImpactAnalysis"
    instanceshape = "oslc:instanceShape"
    integrationstep = "cq:IntegrationStep"
    internalcomment = "cq:InternalComment"
    is_active = "cq:is_active"
    ispilot = "cq:isPilot"
    lastlifecyclestate = "cq:LastLifeCycleState"
    lastminutechange = "cq:LastMinuteChange"
    lastmodifieddate = "cq:LastModifiedDate"
    latechange = "cq:LateChange"
    lifecyclestate = "cq:LifeCycleState"
    lifecyclestatecomment = "cq:LifeCycleStateComment"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    mappingtoderivatives = "cq:MappingToDerivatives"
    milestones = "cq:Milestones"
    note_entry = "cq:Note_Entry"
    notes_log = "cq:Notes_Log"
    operationcontext = "cq:OperationContext"
    operationmode = "cq:OperationMode"
    oslccommand = "cq:oslcCommand"
    planningaction = "cq:PlanningAction"
    priority = "cq:Priority"
    prioritycomment = "cq:PriorityComment"
    qualificationmeasure = "cq:QualificationMeasure"
    qualificationstatus = "cq:QualificationStatus"
    ratl_forminfo = "cq:ratl_forminfo"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    requestedconfiguration = "cq:RequestedConfiguration"
    scope = "cq:Scope"
    selectassignee = "cq:SelectAssignee"
    shorttitle = "oslc:shortTitle"
    subject = "dcterms:subject"
    submitdate = "cq:SubmitDate"
    submitter = "cq:Submitter"
    suppressvalidation = "cq:SuppressValidation"
    suppressvalidationcomment = "cq:SuppressValidationComment"
    swarchitecturedecision = "cq:SwArchitectureDecision"
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


def create_issuereleasemap_changeset(
    accountnumbers: typing.Optional[str] = None,
    actualconfiguration: typing.Optional[str] = None,
    assignee: typing.Optional[typing.Union[Resource, 'Users']] = None,
    assigneefilter: typing.Optional[str] = None,
    assigneesearch: typing.Optional[str] = None,
    changestoconfiguration: typing.Optional[str] = None,
    changestoissues: typing.Optional[str] = None,
    changestopartlist: typing.Optional[str] = None,
    commercialassignee: typing.Optional[typing.Union[Resource, 'Users']] = None,
    commercialassigneefilter: typing.Optional[str] = None,
    commercialassigneesearch: typing.Optional[str] = None,
    cq__Description: typing.Optional[str] = None,
    createcommercial: typing.Optional[typing.Literal["No", "Yes"]] = None,
    creationmode: typing.Optional[str] = None,
    creator: typing.Optional[Resource] = None,
    dbid: typing.Optional[str] = None,
    dcterms__description: typing.Optional[str] = None,
    external_id: typing.Optional[str] = None,
    externalassignee: typing.Optional[typing.Union[Resource, 'Contact']] = None,
    externalcomment: typing.Optional[str] = None,
    externalcommentauthor: typing.Optional[typing.Union[Resource, 'Users']] = None,
    externalconversation: typing.Optional[str] = None,
    externaldescription: typing.Optional[str] = None,
    externalexchangeattach: typing.Optional[str] = None,
    externalexchangeformat: typing.Optional[str] = None,
    externalexchangeworkflow: typing.Optional[str] = None,
    externalhistory: typing.Optional[str] = None,
    externalinformation: typing.Optional[str] = None,
    externallastexporteddate: typing.Optional[datetime] = None,
    externallastimporteddate: typing.Optional[datetime] = None,
    externalnextstate: typing.Optional[str] = None,
    externalreview: typing.Optional[str] = None,
    externalstate: typing.Optional[str] = None,
    externalstate_parallel1: typing.Optional[str] = None,
    externalstate_parallel2: typing.Optional[str] = None,
    externaltags: typing.Optional[str] = None,
    externaltitle: typing.Optional[str] = None,
    externalupdateversion: typing.Optional[str] = None,
    hasattachmentmappings: typing.Optional[typing.List[typing.Union[Resource, 'Attachmentmapping']]] = None,
    hasbackground: typing.Optional[typing.Union[Resource, 'Background']] = None,
    hascommercialdata: typing.Optional[typing.Union[Resource, 'Commercial']] = None,
    hasexternallinks: typing.Optional[typing.List[typing.Union[Resource, 'Externallink']]] = None,
    hashistorylogs: typing.Optional[typing.List[typing.Union[Resource, 'Historylog']]] = None,
    hasmappedissue: typing.Optional[typing.Union[Resource, 'Issue']] = None,
    hasmappedissuefilter: typing.Optional[str] = None,
    hasmappedissuesearch: typing.Optional[str] = None,
    hasmappedrelease: typing.Optional[typing.Union[Resource, 'Release']] = None,
    hasmappedreleasefilter: typing.Optional[str] = None,
    hasmappedreleasesearch: typing.Optional[str] = None,
    history: typing.Optional[Resource] = None,
    history_log: typing.Optional[str] = None,
    id: typing.Optional[str] = None,
    identifier: typing.Optional[str] = None,
    impactanalysis: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    integrationstep: typing.Optional[str] = None,
    internalcomment: typing.Optional[str] = None,
    is_active: typing.Optional[int] = None,
    ispilot: typing.Optional[str] = None,
    lastlifecyclestate: typing.Optional[str] = None,
    lastminutechange: typing.Optional[str] = None,
    lastmodifieddate: typing.Optional[datetime] = None,
    latechange: typing.Optional[str] = None,
    lifecyclestate: typing.Optional[str] = None,
    lifecyclestatecomment: typing.Optional[str] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    mappingtoderivatives: typing.Optional[str] = None,
    milestones: typing.Optional[str] = None,
    note_entry: typing.Optional[str] = None,
    notes_log: typing.Optional[str] = None,
    operationcontext: typing.Optional[str] = None,
    operationmode: typing.Optional[str] = None,
    oslccommand: typing.Optional[str] = None,
    planningaction: typing.Optional[str] = None,
    priority: typing.Optional[str] = None,
    prioritycomment: typing.Optional[str] = None,
    qualificationmeasure: typing.Optional[str] = None,
    qualificationstatus: typing.Optional[str] = None,
    ratl_forminfo: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    requestedconfiguration: typing.Optional[str] = None,
    scope: typing.Optional[str] = None,
    selectassignee: typing.Optional[str] = None,
    shorttitle: typing.Optional[str] = None,
    subject: typing.Optional[typing.List[str]] = None,
    submitdate: typing.Optional[datetime] = None,
    submitter: typing.Optional[str] = None,
    suppressvalidation: typing.Optional[str] = None,
    suppressvalidationcomment: typing.Optional[str] = None,
    swarchitecturedecision: typing.Optional[str] = None,
    tags: typing.Optional[str] = None,
    title: typing.Optional[str] = None,
    tld: typing.Optional[str] = None,
    toolversion: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    workflowstatus: typing.Optional[str] = None,
    
) -> Issuereleasemap:
    '''
    Create a Issuereleasemap changeset.
    '''
    fields = {}
    if accountnumbers is not None:
        fields["cq:AccountNumbers"] = accountnumbers 
    if actualconfiguration is not None:
        fields["cq:ActualConfiguration"] = actualconfiguration 
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
    if commercialassignee is not None:
        fields["cq:CommercialAssignee"] = commercialassignee 
    if commercialassigneefilter is not None:
        fields["cq:CommercialAssigneeFilter"] = commercialassigneefilter 
    if commercialassigneesearch is not None:
        fields["cq:CommercialAssigneeSearch"] = commercialassigneesearch 
    if cq__Description is not None:
        fields["cq:Description"] = cq__Description 
    if createcommercial is not None:
        fields["cq:CreateCommercial"] = createcommercial 
    if creationmode is not None:
        fields["cq:CreationMode"] = creationmode 
    if creator is not None:
        fields["dcterms:creator"] = creator 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if dcterms__description is not None:
        fields["dcterms:description"] = dcterms__description 
    if external_id is not None:
        fields["cq:External_ID"] = external_id 
    if externalassignee is not None:
        fields["cq:ExternalAssignee"] = externalassignee 
    if externalcomment is not None:
        fields["cq:ExternalComment"] = externalcomment 
    if externalcommentauthor is not None:
        fields["cq:ExternalCommentAuthor"] = externalcommentauthor 
    if externalconversation is not None:
        fields["cq:ExternalConversation"] = externalconversation 
    if externaldescription is not None:
        fields["cq:ExternalDescription"] = externaldescription 
    if externalexchangeattach is not None:
        fields["cq:ExternalExchangeAttach"] = externalexchangeattach 
    if externalexchangeformat is not None:
        fields["cq:ExternalExchangeFormat"] = externalexchangeformat 
    if externalexchangeworkflow is not None:
        fields["cq:ExternalExchangeWorkflow"] = externalexchangeworkflow 
    if externalhistory is not None:
        fields["cq:ExternalHistory"] = externalhistory 
    if externalinformation is not None:
        fields["cq:ExternalInformation"] = externalinformation 
    if externallastexporteddate is not None:
        fields["cq:ExternalLastExportedDate"] = externallastexporteddate 
    if externallastimporteddate is not None:
        fields["cq:ExternalLastImportedDate"] = externallastimporteddate 
    if externalnextstate is not None:
        fields["cq:ExternalNextState"] = externalnextstate 
    if externalreview is not None:
        fields["cq:ExternalReview"] = externalreview 
    if externalstate is not None:
        fields["cq:ExternalState"] = externalstate 
    if externalstate_parallel1 is not None:
        fields["cq:ExternalState_Parallel1"] = externalstate_parallel1 
    if externalstate_parallel2 is not None:
        fields["cq:ExternalState_Parallel2"] = externalstate_parallel2 
    if externaltags is not None:
        fields["cq:ExternalTags"] = externaltags 
    if externaltitle is not None:
        fields["cq:ExternalTitle"] = externaltitle 
    if externalupdateversion is not None:
        fields["cq:ExternalUpdateVersion"] = externalupdateversion 
    if hasattachmentmappings is not None:
        fields["cq:hasAttachmentMappings"] = hasattachmentmappings 
    if hasbackground is not None:
        fields["cq:hasBackground"] = hasbackground 
    if hascommercialdata is not None:
        fields["cq:hasCommercialData"] = hascommercialdata 
    if hasexternallinks is not None:
        fields["cq:hasExternalLinks"] = hasexternallinks 
    if hashistorylogs is not None:
        fields["cq:hasHistoryLogs"] = hashistorylogs 
    if hasmappedissue is not None:
        fields["cq:hasMappedIssue"] = hasmappedissue 
    if hasmappedissuefilter is not None:
        fields["cq:hasMappedIssueFilter"] = hasmappedissuefilter 
    if hasmappedissuesearch is not None:
        fields["cq:hasMappedIssueSearch"] = hasmappedissuesearch 
    if hasmappedrelease is not None:
        fields["cq:hasMappedRelease"] = hasmappedrelease 
    if hasmappedreleasefilter is not None:
        fields["cq:hasMappedReleaseFilter"] = hasmappedreleasefilter 
    if hasmappedreleasesearch is not None:
        fields["cq:hasMappedReleaseSearch"] = hasmappedreleasesearch 
    if history is not None:
        fields["cq:history"] = history 
    if history_log is not None:
        fields["cq:History_Log"] = history_log 
    if id is not None:
        fields["cq:id"] = id 
    if identifier is not None:
        fields["dcterms:identifier"] = identifier 
    if impactanalysis is not None:
        fields["cq:ImpactAnalysis"] = impactanalysis 
    if instanceshape is not None:
        fields["oslc:instanceShape"] = instanceshape 
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
    if lastminutechange is not None:
        fields["cq:LastMinuteChange"] = lastminutechange 
    if lastmodifieddate is not None:
        fields["cq:LastModifiedDate"] = lastmodifieddate 
    if latechange is not None:
        fields["cq:LateChange"] = latechange 
    if lifecyclestate is not None:
        fields["cq:LifeCycleState"] = lifecyclestate 
    if lifecyclestatecomment is not None:
        fields["cq:LifeCycleStateComment"] = lifecyclestatecomment 
    if lock_version is not None:
        fields["cq:lock_version"] = lock_version 
    if locked_by is not None:
        fields["cq:locked_by"] = locked_by 
    if mappingtoderivatives is not None:
        fields["cq:MappingToDerivatives"] = mappingtoderivatives 
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
    if planningaction is not None:
        fields["cq:PlanningAction"] = planningaction 
    if priority is not None:
        fields["cq:Priority"] = priority 
    if prioritycomment is not None:
        fields["cq:PriorityComment"] = prioritycomment 
    if qualificationmeasure is not None:
        fields["cq:QualificationMeasure"] = qualificationmeasure 
    if qualificationstatus is not None:
        fields["cq:QualificationStatus"] = qualificationstatus 
    if ratl_forminfo is not None:
        fields["cq:ratl_forminfo"] = ratl_forminfo 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if requestedconfiguration is not None:
        fields["cq:RequestedConfiguration"] = requestedconfiguration 
    if scope is not None:
        fields["cq:Scope"] = scope 
    if selectassignee is not None:
        fields["cq:SelectAssignee"] = selectassignee 
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
    if swarchitecturedecision is not None:
        fields["cq:SwArchitectureDecision"] = swarchitecturedecision 
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
    

    fields["dcterms:type"] = "Issuereleasemap"
    return Issuereleasemap(**fields)