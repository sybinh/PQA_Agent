
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

    from .project import Project

    from .users import Users

    from .releasereleasemap import Releasereleasemap

    from .issuereleasemap import Issuereleasemap

    from .issue import Issue

    from .workitem import Workitem

    from .background import Background

    from .ratl_replicas import Ratl_Replicas

    from .contact import Contact



class Release(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    accountnumbers: typing.Optional[str] = Field(alias="cq:AccountNumbers", default=None)
    
    actualdate: typing.Optional[datetime] = Field(alias="cq:ActualDate", default=None)
    
    approval: typing.Optional[str] = Field(alias="cq:Approval", default=None)
    
    assignee: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:Assignee", default=None)
    
    assigneefilter: typing.Optional[str] = Field(alias="cq:AssigneeFilter", default=None)
    
    assigneesearch: typing.Optional[str] = Field(alias="cq:AssigneeSearch", default=None)
    
    attachments: typing.Optional[Resource] = Field(alias="cq:Attachments", default=None)
    
    basedonpredecessor: typing.Optional[typing.Literal["No", "Yes"]] = Field(alias="cq:BasedOnPredecessor", default=None)
    
    belongstoproject: typing.Optional[typing.Union[Resource, 'Project']] = Field(alias="cq:belongsToProject", default=None)
    
    belongstoprojectfilter: typing.Optional[str] = Field(alias="cq:belongsToProjectFilter", default=None)
    
    belongstoprojectsearch: typing.Optional[str] = Field(alias="cq:belongsToProjectSearch", default=None)
    
    bulkselection: typing.Optional[str] = Field(alias="cq:BulkSelection", default=None)
    
    category: typing.Optional[str] = Field(alias="cq:Category", default=None)
    
    classification: typing.Optional[str] = Field(alias="cq:Classification", default=None)
    
    collaboration: typing.Optional[str] = Field(alias="cq:Collaboration", default=None)
    
    cq__Description: typing.Optional[str] = Field(alias="cq:Description", default=None)
    
    cq__Title: typing.Optional[str] = Field(alias="cq:Title", default=None)
    
    cq__Type: typing.Optional[str] = Field(alias="cq:Type", default=None)
    
    creationmode: typing.Optional[str] = Field(alias="cq:CreationMode", default=None)
    
    creator: typing.Optional[Resource] = Field(alias="dcterms:creator", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__description: typing.Optional[str] = Field(alias="dcterms:description", default=None)
    
    dcterms__title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    derivatives: typing.Optional[str] = Field(alias="cq:Derivatives", default=None)
    
    developmentmethod: typing.Optional[str] = Field(alias="cq:DevelopmentMethod", default=None)
    
    domain: typing.Optional[str] = Field(alias="cq:Domain", default=None)
    
    enablebulkoperations: typing.Optional[str] = Field(alias="cq:EnableBulkOperations", default=None)
    
    estimatedeffort: typing.Optional[int] = Field(alias="cq:EstimatedEffort", default=None)
    
    estimationcomment: typing.Optional[str] = Field(alias="cq:EstimationComment", default=None)
    
    external_id: typing.Optional[str] = Field(alias="cq:External_ID", default=None)
    
    externalassignee: typing.Optional[typing.Union[Resource, 'Contact']] = Field(alias="cq:ExternalAssignee", default=None)
    
    externalcomment: typing.Optional[str] = Field(alias="cq:ExternalComment", default=None)
    
    externaldescription: typing.Optional[str] = Field(alias="cq:ExternalDescription", default=None)
    
    externalinformation: typing.Optional[str] = Field(alias="cq:ExternalInformation", default=None)
    
    externalstate: typing.Optional[str] = Field(alias="cq:ExternalState", default=None)
    
    externalsubmitter: typing.Optional[typing.Union[Resource, 'Contact']] = Field(alias="cq:ExternalSubmitter", default=None)
    
    externaltags: typing.Optional[str] = Field(alias="cq:ExternalTags", default=None)
    
    externaltitle: typing.Optional[str] = Field(alias="cq:ExternalTitle", default=None)
    
    fulltextsearchtitle: typing.Optional[str] = Field(alias="cq:FullTextSearchTitle", default=None)
    
    hasbackground: typing.Optional[typing.Union[Resource, 'Background']] = Field(alias="cq:hasBackground", default=None)
    
    hasexternallinks: typing.List[typing.Union[Resource, 'Externallink']] = Field(alias="cq:hasExternalLinks", default_factory=lambda: [])
    
    hashistorylogs: typing.List[typing.Union[Resource, 'Historylog']] = Field(alias="cq:hasHistoryLogs", default_factory=lambda: [])
    
    hasmappedchildren: typing.List[typing.Union[Resource, 'Releasereleasemap']] = Field(alias="cq:hasMappedChildren", default_factory=lambda: [])
    
    hasmappedissues: typing.List[typing.Union[Resource, 'Issuereleasemap']] = Field(alias="cq:hasMappedIssues", default_factory=lambda: [])
    
    hasmappedparents: typing.List[typing.Union[Resource, 'Releasereleasemap']] = Field(alias="cq:hasMappedParents", default_factory=lambda: [])
    
    haspredecessor: typing.Optional[typing.Union[Resource, 'Release']] = Field(alias="cq:hasPredecessor", default=None)
    
    haspredecessorfilter: typing.Optional[str] = Field(alias="cq:hasPredecessorFilter", default=None)
    
    haspredecessorsearch: typing.Optional[str] = Field(alias="cq:hasPredecessorSearch", default=None)
    
    hassuccessor: typing.List[typing.Union[Resource, 'Release']] = Field(alias="cq:hasSuccessor", default_factory=lambda: [])
    
    hasworkitems: typing.List[typing.Union[Resource, 'Workitem']] = Field(alias="cq:hasWorkitems", default_factory=lambda: [])
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    history_log: typing.Optional[str] = Field(alias="cq:History_Log", default=None)
    
    id: typing.Optional[str] = Field(alias="cq:id", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    implementationfreeze: typing.Optional[datetime] = Field(alias="cq:ImplementationFreeze", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    internalcomment: typing.Optional[str] = Field(alias="cq:InternalComment", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    is_duplicate: typing.Optional[int] = Field(alias="cq:is_duplicate", default=None)
    
    isaffectedbydefectissues: typing.List[typing.Union[Resource, 'Issue']] = Field(alias="cq:isAffectedByDefectIssues", default_factory=lambda: [])
    
    lastlifecyclestate: typing.Optional[str] = Field(alias="cq:LastLifeCycleState", default=None)
    
    lastmodifieddate: typing.Optional[datetime] = Field(alias="cq:LastModifiedDate", default=None)
    
    lifecyclestate: typing.Optional[str] = Field(alias="cq:LifeCycleState", default=None)
    
    lifecyclestatecomment: typing.Optional[str] = Field(alias="cq:LifeCycleStateComment", default=None)
    
    links: typing.Optional[str] = Field(alias="cq:Links", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    milestones: typing.Optional[str] = Field(alias="cq:Milestones", default=None)
    
    note_entry: typing.Optional[str] = Field(alias="cq:Note_Entry", default=None)
    
    notes_log: typing.Optional[str] = Field(alias="cq:Notes_Log", default=None)
    
    operationcontext: typing.Optional[str] = Field(alias="cq:OperationContext", default=None)
    
    operationmode: typing.Optional[str] = Field(alias="cq:OperationMode", default=None)
    
    oslccommand: typing.Optional[str] = Field(alias="cq:oslcCommand", default=None)
    
    planneddate: typing.Optional[datetime] = Field(alias="cq:PlannedDate", default=None)
    
    planningbaseline: typing.Optional[str] = Field(alias="cq:PlanningBaseline", default=None)
    
    planningfreeze: typing.Optional[datetime] = Field(alias="cq:PlanningFreeze", default=None)
    
    planninggranularity: typing.Optional[str] = Field(alias="cq:PlanningGranularity", default=None)
    
    processtailoring: typing.Optional[str] = Field(alias="cq:ProcessTailoring", default=None)
    
    product: typing.Optional[str] = Field(alias="cq:Product", default=None)
    
    projectid: typing.Optional[str] = Field(alias="cq:ProjectId", default=None)
    
    ratl_forminfo: typing.Optional[str] = Field(alias="cq:ratl_forminfo", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    release_sdom: typing.Optional[str] = Field(alias="cq:Release_SDOM", default=None)
    
    release_sdom_userid: typing.Optional[str] = Field(alias="cq:Release_SDOM_UserID", default=None)
    
    requester: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:Requester", default=None)
    
    requesterfilter: typing.Optional[str] = Field(alias="cq:RequesterFilter", default=None)
    
    requestersearch: typing.Optional[str] = Field(alias="cq:RequesterSearch", default=None)
    
    rotitle: typing.Optional[str] = Field(alias="cq:ROTitle", default=None)
    
    rotype: typing.Optional[str] = Field(alias="cq:ROType", default=None)
    
    scmcomment: typing.Optional[str] = Field(alias="cq:SCMComment", default=None)
    
    scmsystem: typing.Optional[str] = Field(alias="cq:SCMSystem", default=None)
    
    scope: typing.Optional[str] = Field(alias="cq:Scope", default=None)
    
    selectassignee: typing.Optional[str] = Field(alias="cq:SelectAssignee", default=None)
    
    shorttitle: typing.Optional[str] = Field(alias="oslc:shortTitle", default=None)
    
    specificationfreeze: typing.Optional[datetime] = Field(alias="cq:SpecificationFreeze", default=None)
    
    startdate: typing.Optional[datetime] = Field(alias="cq:StartDate", default=None)
    
    state: typing.Optional[str] = Field(alias="cq:State", default=None)
    
    status: typing.Optional[str] = Field(alias="oslc_cm:status", default=None)
    
    subject: typing.List[str] = Field(alias="dcterms:subject", default_factory=lambda: [])
    
    submitdate: typing.Optional[datetime] = Field(alias="cq:SubmitDate", default=None)
    
    submitter: typing.Optional[str] = Field(alias="cq:Submitter", default=None)
    
    supplierrequestedbaseline: typing.Optional[str] = Field(alias="cq:SupplierRequestedBaseline", default=None)
    
    suppressvalidation: typing.Optional[str] = Field(alias="cq:SuppressValidation", default=None)
    
    suppressvalidationcomment: typing.Optional[str] = Field(alias="cq:SuppressValidationComment", default=None)
    
    swarchitecturedecision: typing.Optional[str] = Field(alias="cq:SwArchitectureDecision", default=None)
    
    swtest_bft: typing.Optional[str] = Field(alias="cq:SWTest_BFT", default=None)
    
    swtest_com: typing.Optional[str] = Field(alias="cq:SWTest_COM", default=None)
    
    swtest_cst: typing.Optional[str] = Field(alias="cq:SWTest_CST", default=None)
    
    swtest_eeprom: typing.Optional[str] = Field(alias="cq:SWTest_EEPROM", default=None)
    
    swtest_ft: typing.Optional[str] = Field(alias="cq:SWTest_FT", default=None)
    
    swtest_io: typing.Optional[str] = Field(alias="cq:SWTest_IO", default=None)
    
    swtest_opt: typing.Optional[str] = Field(alias="cq:SWTest_OPT", default=None)
    
    swtest_ost: typing.Optional[str] = Field(alias="cq:SWTest_OST", default=None)
    
    swtest_pver_conf: typing.Optional[str] = Field(alias="cq:SWTest_PVER_Conf", default=None)
    
    swtest_pver_i: typing.Optional[str] = Field(alias="cq:SWTest_PVER_I", default=None)
    
    swtest_pverf: typing.Optional[str] = Field(alias="cq:SWTest_PVERF", default=None)
    
    swtest_pveri: typing.Optional[str] = Field(alias="cq:SWTest_PVERI", default=None)
    
    swtest_robustness: typing.Optional[str] = Field(alias="cq:SWTest_Robustness", default=None)
    
    swtest_srr: typing.Optional[str] = Field(alias="cq:SWTest_SRR", default=None)
    
    swtest_viva: typing.Optional[str] = Field(alias="cq:SWTest_VIVA", default=None)
    
    sync: typing.Optional[str] = Field(alias="cq:Sync", default=None)
    
    tags: typing.Optional[str] = Field(alias="cq:Tags", default=None)
    
    template: typing.Optional[str] = Field(alias="cq:Template", default=None)
    
    tld: typing.Optional[str] = Field(alias="cq:TLD", default=None)
    
    toolversion: typing.Optional[str] = Field(alias="cq:ToolVersion", default=None)
    
    unduplicate_state: typing.Optional[str] = Field(alias="cq:unduplicate_state", default=None)
    
    validationexceptcomment: typing.Optional[str] = Field(alias="cq:ValidationExceptComment", default=None)
    
    validationexceptions: typing.Optional[str] = Field(alias="cq:ValidationExceptions", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    
    workflowstatus: typing.Optional[str] = Field(alias="cq:WorkflowStatus", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16777230"

    @classmethod
    def shape_name(cls) -> str:
        return "Release"

    @field_validator('*', mode="before")
    @classmethod
    def empty_str_to_none(cls, v):
        if v == '':
            return None
        return v

    
    @field_validator('instanceshape', 'history', 'attachments', mode="before")
    @classmethod
    def list_to_resource(cls, v):
        if isinstance(v, list):
            return v[0]
        return v
    


class ReleaseProperty(Enum):
    accountnumbers = "cq:AccountNumbers"
    actualdate = "cq:ActualDate"
    approval = "cq:Approval"
    assignee = "cq:Assignee"
    assigneefilter = "cq:AssigneeFilter"
    assigneesearch = "cq:AssigneeSearch"
    attachments = "cq:Attachments"
    basedonpredecessor = "cq:BasedOnPredecessor"
    belongstoproject = "cq:belongsToProject"
    belongstoprojectfilter = "cq:belongsToProjectFilter"
    belongstoprojectsearch = "cq:belongsToProjectSearch"
    bulkselection = "cq:BulkSelection"
    category = "cq:Category"
    classification = "cq:Classification"
    collaboration = "cq:Collaboration"
    cq__Description = "cq:Description"
    cq__Title = "cq:Title"
    cq__Type = "cq:Type"
    creationmode = "cq:CreationMode"
    creator = "dcterms:creator"
    dbid = "cq:dbid"
    dcterms__description = "dcterms:description"
    dcterms__title = "dcterms:title"
    dcterms__type = "dcterms:type"
    derivatives = "cq:Derivatives"
    developmentmethod = "cq:DevelopmentMethod"
    domain = "cq:Domain"
    enablebulkoperations = "cq:EnableBulkOperations"
    estimatedeffort = "cq:EstimatedEffort"
    estimationcomment = "cq:EstimationComment"
    external_id = "cq:External_ID"
    externalassignee = "cq:ExternalAssignee"
    externalcomment = "cq:ExternalComment"
    externaldescription = "cq:ExternalDescription"
    externalinformation = "cq:ExternalInformation"
    externalstate = "cq:ExternalState"
    externalsubmitter = "cq:ExternalSubmitter"
    externaltags = "cq:ExternalTags"
    externaltitle = "cq:ExternalTitle"
    fulltextsearchtitle = "cq:FullTextSearchTitle"
    hasbackground = "cq:hasBackground"
    hasexternallinks = "cq:hasExternalLinks"
    hashistorylogs = "cq:hasHistoryLogs"
    hasmappedchildren = "cq:hasMappedChildren"
    hasmappedissues = "cq:hasMappedIssues"
    hasmappedparents = "cq:hasMappedParents"
    haspredecessor = "cq:hasPredecessor"
    haspredecessorfilter = "cq:hasPredecessorFilter"
    haspredecessorsearch = "cq:hasPredecessorSearch"
    hassuccessor = "cq:hasSuccessor"
    hasworkitems = "cq:hasWorkitems"
    history = "cq:history"
    history_log = "cq:History_Log"
    id = "cq:id"
    identifier = "dcterms:identifier"
    implementationfreeze = "cq:ImplementationFreeze"
    instanceshape = "oslc:instanceShape"
    internalcomment = "cq:InternalComment"
    is_active = "cq:is_active"
    is_duplicate = "cq:is_duplicate"
    isaffectedbydefectissues = "cq:isAffectedByDefectIssues"
    lastlifecyclestate = "cq:LastLifeCycleState"
    lastmodifieddate = "cq:LastModifiedDate"
    lifecyclestate = "cq:LifeCycleState"
    lifecyclestatecomment = "cq:LifeCycleStateComment"
    links = "cq:Links"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    milestones = "cq:Milestones"
    note_entry = "cq:Note_Entry"
    notes_log = "cq:Notes_Log"
    operationcontext = "cq:OperationContext"
    operationmode = "cq:OperationMode"
    oslccommand = "cq:oslcCommand"
    planneddate = "cq:PlannedDate"
    planningbaseline = "cq:PlanningBaseline"
    planningfreeze = "cq:PlanningFreeze"
    planninggranularity = "cq:PlanningGranularity"
    processtailoring = "cq:ProcessTailoring"
    product = "cq:Product"
    projectid = "cq:ProjectId"
    ratl_forminfo = "cq:ratl_forminfo"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    release_sdom = "cq:Release_SDOM"
    release_sdom_userid = "cq:Release_SDOM_UserID"
    requester = "cq:Requester"
    requesterfilter = "cq:RequesterFilter"
    requestersearch = "cq:RequesterSearch"
    rotitle = "cq:ROTitle"
    rotype = "cq:ROType"
    scmcomment = "cq:SCMComment"
    scmsystem = "cq:SCMSystem"
    scope = "cq:Scope"
    selectassignee = "cq:SelectAssignee"
    shorttitle = "oslc:shortTitle"
    specificationfreeze = "cq:SpecificationFreeze"
    startdate = "cq:StartDate"
    state = "cq:State"
    status = "oslc_cm:status"
    subject = "dcterms:subject"
    submitdate = "cq:SubmitDate"
    submitter = "cq:Submitter"
    supplierrequestedbaseline = "cq:SupplierRequestedBaseline"
    suppressvalidation = "cq:SuppressValidation"
    suppressvalidationcomment = "cq:SuppressValidationComment"
    swarchitecturedecision = "cq:SwArchitectureDecision"
    swtest_bft = "cq:SWTest_BFT"
    swtest_com = "cq:SWTest_COM"
    swtest_cst = "cq:SWTest_CST"
    swtest_eeprom = "cq:SWTest_EEPROM"
    swtest_ft = "cq:SWTest_FT"
    swtest_io = "cq:SWTest_IO"
    swtest_opt = "cq:SWTest_OPT"
    swtest_ost = "cq:SWTest_OST"
    swtest_pver_conf = "cq:SWTest_PVER_Conf"
    swtest_pver_i = "cq:SWTest_PVER_I"
    swtest_pverf = "cq:SWTest_PVERF"
    swtest_pveri = "cq:SWTest_PVERI"
    swtest_robustness = "cq:SWTest_Robustness"
    swtest_srr = "cq:SWTest_SRR"
    swtest_viva = "cq:SWTest_VIVA"
    sync = "cq:Sync"
    tags = "cq:Tags"
    template = "cq:Template"
    tld = "cq:TLD"
    toolversion = "cq:ToolVersion"
    unduplicate_state = "cq:unduplicate_state"
    validationexceptcomment = "cq:ValidationExceptComment"
    validationexceptions = "cq:ValidationExceptions"
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


def create_release_changeset(
    accountnumbers: typing.Optional[str] = None,
    actualdate: typing.Optional[datetime] = None,
    approval: typing.Optional[str] = None,
    assignee: typing.Optional[typing.Union[Resource, 'Users']] = None,
    assigneefilter: typing.Optional[str] = None,
    assigneesearch: typing.Optional[str] = None,
    attachments: typing.Optional[Resource] = None,
    basedonpredecessor: typing.Optional[typing.Literal["No", "Yes"]] = None,
    belongstoproject: typing.Optional[typing.Union[Resource, 'Project']] = None,
    belongstoprojectfilter: typing.Optional[str] = None,
    belongstoprojectsearch: typing.Optional[str] = None,
    bulkselection: typing.Optional[str] = None,
    category: typing.Optional[str] = None,
    classification: typing.Optional[str] = None,
    collaboration: typing.Optional[str] = None,
    cq__Description: typing.Optional[str] = None,
    cq__Title: typing.Optional[str] = None,
    cq__Type: typing.Optional[str] = None,
    creationmode: typing.Optional[str] = None,
    creator: typing.Optional[Resource] = None,
    dbid: typing.Optional[str] = None,
    dcterms__description: typing.Optional[str] = None,
    dcterms__title: typing.Optional[str] = None,
    derivatives: typing.Optional[str] = None,
    developmentmethod: typing.Optional[str] = None,
    domain: typing.Optional[str] = None,
    enablebulkoperations: typing.Optional[str] = None,
    estimatedeffort: typing.Optional[int] = None,
    estimationcomment: typing.Optional[str] = None,
    external_id: typing.Optional[str] = None,
    externalassignee: typing.Optional[typing.Union[Resource, 'Contact']] = None,
    externalcomment: typing.Optional[str] = None,
    externaldescription: typing.Optional[str] = None,
    externalinformation: typing.Optional[str] = None,
    externalstate: typing.Optional[str] = None,
    externalsubmitter: typing.Optional[typing.Union[Resource, 'Contact']] = None,
    externaltags: typing.Optional[str] = None,
    externaltitle: typing.Optional[str] = None,
    fulltextsearchtitle: typing.Optional[str] = None,
    hasbackground: typing.Optional[typing.Union[Resource, 'Background']] = None,
    hasexternallinks: typing.Optional[typing.List[typing.Union[Resource, 'Externallink']]] = None,
    hashistorylogs: typing.Optional[typing.List[typing.Union[Resource, 'Historylog']]] = None,
    hasmappedchildren: typing.Optional[typing.List[typing.Union[Resource, 'Releasereleasemap']]] = None,
    hasmappedissues: typing.Optional[typing.List[typing.Union[Resource, 'Issuereleasemap']]] = None,
    hasmappedparents: typing.Optional[typing.List[typing.Union[Resource, 'Releasereleasemap']]] = None,
    haspredecessor: typing.Optional[typing.Union[Resource, 'Release']] = None,
    haspredecessorfilter: typing.Optional[str] = None,
    haspredecessorsearch: typing.Optional[str] = None,
    hassuccessor: typing.Optional[typing.List[typing.Union[Resource, 'Release']]] = None,
    hasworkitems: typing.Optional[typing.List[typing.Union[Resource, 'Workitem']]] = None,
    history: typing.Optional[Resource] = None,
    history_log: typing.Optional[str] = None,
    id: typing.Optional[str] = None,
    identifier: typing.Optional[str] = None,
    implementationfreeze: typing.Optional[datetime] = None,
    instanceshape: typing.Optional[Resource] = None,
    internalcomment: typing.Optional[str] = None,
    is_active: typing.Optional[int] = None,
    is_duplicate: typing.Optional[int] = None,
    isaffectedbydefectissues: typing.Optional[typing.List[typing.Union[Resource, 'Issue']]] = None,
    lastlifecyclestate: typing.Optional[str] = None,
    lastmodifieddate: typing.Optional[datetime] = None,
    lifecyclestate: typing.Optional[str] = None,
    lifecyclestatecomment: typing.Optional[str] = None,
    links: typing.Optional[str] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    milestones: typing.Optional[str] = None,
    note_entry: typing.Optional[str] = None,
    notes_log: typing.Optional[str] = None,
    operationcontext: typing.Optional[str] = None,
    operationmode: typing.Optional[str] = None,
    oslccommand: typing.Optional[str] = None,
    planneddate: typing.Optional[datetime] = None,
    planningbaseline: typing.Optional[str] = None,
    planningfreeze: typing.Optional[datetime] = None,
    planninggranularity: typing.Optional[str] = None,
    processtailoring: typing.Optional[str] = None,
    product: typing.Optional[str] = None,
    projectid: typing.Optional[str] = None,
    ratl_forminfo: typing.Optional[str] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    release_sdom: typing.Optional[str] = None,
    release_sdom_userid: typing.Optional[str] = None,
    requester: typing.Optional[typing.Union[Resource, 'Users']] = None,
    requesterfilter: typing.Optional[str] = None,
    requestersearch: typing.Optional[str] = None,
    rotitle: typing.Optional[str] = None,
    rotype: typing.Optional[str] = None,
    scmcomment: typing.Optional[str] = None,
    scmsystem: typing.Optional[str] = None,
    scope: typing.Optional[str] = None,
    selectassignee: typing.Optional[str] = None,
    shorttitle: typing.Optional[str] = None,
    specificationfreeze: typing.Optional[datetime] = None,
    startdate: typing.Optional[datetime] = None,
    state: typing.Optional[str] = None,
    status: typing.Optional[str] = None,
    subject: typing.Optional[typing.List[str]] = None,
    submitdate: typing.Optional[datetime] = None,
    submitter: typing.Optional[str] = None,
    supplierrequestedbaseline: typing.Optional[str] = None,
    suppressvalidation: typing.Optional[str] = None,
    suppressvalidationcomment: typing.Optional[str] = None,
    swarchitecturedecision: typing.Optional[str] = None,
    swtest_bft: typing.Optional[str] = None,
    swtest_com: typing.Optional[str] = None,
    swtest_cst: typing.Optional[str] = None,
    swtest_eeprom: typing.Optional[str] = None,
    swtest_ft: typing.Optional[str] = None,
    swtest_io: typing.Optional[str] = None,
    swtest_opt: typing.Optional[str] = None,
    swtest_ost: typing.Optional[str] = None,
    swtest_pver_conf: typing.Optional[str] = None,
    swtest_pver_i: typing.Optional[str] = None,
    swtest_pverf: typing.Optional[str] = None,
    swtest_pveri: typing.Optional[str] = None,
    swtest_robustness: typing.Optional[str] = None,
    swtest_srr: typing.Optional[str] = None,
    swtest_viva: typing.Optional[str] = None,
    sync: typing.Optional[str] = None,
    tags: typing.Optional[str] = None,
    template: typing.Optional[str] = None,
    tld: typing.Optional[str] = None,
    toolversion: typing.Optional[str] = None,
    unduplicate_state: typing.Optional[str] = None,
    validationexceptcomment: typing.Optional[str] = None,
    validationexceptions: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    workflowstatus: typing.Optional[str] = None,
    
) -> Release:
    '''
    Create a Release changeset.
    '''
    fields = {}
    if accountnumbers is not None:
        fields["cq:AccountNumbers"] = accountnumbers 
    if actualdate is not None:
        fields["cq:ActualDate"] = actualdate 
    if approval is not None:
        fields["cq:Approval"] = approval 
    if assignee is not None:
        fields["cq:Assignee"] = assignee 
    if assigneefilter is not None:
        fields["cq:AssigneeFilter"] = assigneefilter 
    if assigneesearch is not None:
        fields["cq:AssigneeSearch"] = assigneesearch 
    if attachments is not None:
        fields["cq:Attachments"] = attachments 
    if basedonpredecessor is not None:
        fields["cq:BasedOnPredecessor"] = basedonpredecessor 
    if belongstoproject is not None:
        fields["cq:belongsToProject"] = belongstoproject 
    if belongstoprojectfilter is not None:
        fields["cq:belongsToProjectFilter"] = belongstoprojectfilter 
    if belongstoprojectsearch is not None:
        fields["cq:belongsToProjectSearch"] = belongstoprojectsearch 
    if bulkselection is not None:
        fields["cq:BulkSelection"] = bulkselection 
    if category is not None:
        fields["cq:Category"] = category 
    if classification is not None:
        fields["cq:Classification"] = classification 
    if collaboration is not None:
        fields["cq:Collaboration"] = collaboration 
    if cq__Description is not None:
        fields["cq:Description"] = cq__Description 
    if cq__Title is not None:
        fields["cq:Title"] = cq__Title 
    if cq__Type is not None:
        fields["cq:Type"] = cq__Type 
    if creationmode is not None:
        fields["cq:CreationMode"] = creationmode 
    if creator is not None:
        fields["dcterms:creator"] = creator 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if dcterms__description is not None:
        fields["dcterms:description"] = dcterms__description 
    if dcterms__title is not None:
        fields["dcterms:title"] = dcterms__title 
    if derivatives is not None:
        fields["cq:Derivatives"] = derivatives 
    if developmentmethod is not None:
        fields["cq:DevelopmentMethod"] = developmentmethod 
    if domain is not None:
        fields["cq:Domain"] = domain 
    if enablebulkoperations is not None:
        fields["cq:EnableBulkOperations"] = enablebulkoperations 
    if estimatedeffort is not None:
        fields["cq:EstimatedEffort"] = estimatedeffort 
    if estimationcomment is not None:
        fields["cq:EstimationComment"] = estimationcomment 
    if external_id is not None:
        fields["cq:External_ID"] = external_id 
    if externalassignee is not None:
        fields["cq:ExternalAssignee"] = externalassignee 
    if externalcomment is not None:
        fields["cq:ExternalComment"] = externalcomment 
    if externaldescription is not None:
        fields["cq:ExternalDescription"] = externaldescription 
    if externalinformation is not None:
        fields["cq:ExternalInformation"] = externalinformation 
    if externalstate is not None:
        fields["cq:ExternalState"] = externalstate 
    if externalsubmitter is not None:
        fields["cq:ExternalSubmitter"] = externalsubmitter 
    if externaltags is not None:
        fields["cq:ExternalTags"] = externaltags 
    if externaltitle is not None:
        fields["cq:ExternalTitle"] = externaltitle 
    if fulltextsearchtitle is not None:
        fields["cq:FullTextSearchTitle"] = fulltextsearchtitle 
    if hasbackground is not None:
        fields["cq:hasBackground"] = hasbackground 
    if hasexternallinks is not None:
        fields["cq:hasExternalLinks"] = hasexternallinks 
    if hashistorylogs is not None:
        fields["cq:hasHistoryLogs"] = hashistorylogs 
    if hasmappedchildren is not None:
        fields["cq:hasMappedChildren"] = hasmappedchildren 
    if hasmappedissues is not None:
        fields["cq:hasMappedIssues"] = hasmappedissues 
    if hasmappedparents is not None:
        fields["cq:hasMappedParents"] = hasmappedparents 
    if haspredecessor is not None:
        fields["cq:hasPredecessor"] = haspredecessor 
    if haspredecessorfilter is not None:
        fields["cq:hasPredecessorFilter"] = haspredecessorfilter 
    if haspredecessorsearch is not None:
        fields["cq:hasPredecessorSearch"] = haspredecessorsearch 
    if hassuccessor is not None:
        fields["cq:hasSuccessor"] = hassuccessor 
    if hasworkitems is not None:
        fields["cq:hasWorkitems"] = hasworkitems 
    if history is not None:
        fields["cq:history"] = history 
    if history_log is not None:
        fields["cq:History_Log"] = history_log 
    if id is not None:
        fields["cq:id"] = id 
    if identifier is not None:
        fields["dcterms:identifier"] = identifier 
    if implementationfreeze is not None:
        fields["cq:ImplementationFreeze"] = implementationfreeze 
    if instanceshape is not None:
        fields["oslc:instanceShape"] = instanceshape 
    if internalcomment is not None:
        fields["cq:InternalComment"] = internalcomment 
    if is_active is not None:
        fields["cq:is_active"] = is_active 
    if is_duplicate is not None:
        fields["cq:is_duplicate"] = is_duplicate 
    if isaffectedbydefectissues is not None:
        fields["cq:isAffectedByDefectIssues"] = isaffectedbydefectissues 
    if lastlifecyclestate is not None:
        fields["cq:LastLifeCycleState"] = lastlifecyclestate 
    if lastmodifieddate is not None:
        fields["cq:LastModifiedDate"] = lastmodifieddate 
    if lifecyclestate is not None:
        fields["cq:LifeCycleState"] = lifecyclestate 
    if lifecyclestatecomment is not None:
        fields["cq:LifeCycleStateComment"] = lifecyclestatecomment 
    if links is not None:
        fields["cq:Links"] = links 
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
    if planneddate is not None:
        fields["cq:PlannedDate"] = planneddate 
    if planningbaseline is not None:
        fields["cq:PlanningBaseline"] = planningbaseline 
    if planningfreeze is not None:
        fields["cq:PlanningFreeze"] = planningfreeze 
    if planninggranularity is not None:
        fields["cq:PlanningGranularity"] = planninggranularity 
    if processtailoring is not None:
        fields["cq:ProcessTailoring"] = processtailoring 
    if product is not None:
        fields["cq:Product"] = product 
    if projectid is not None:
        fields["cq:ProjectId"] = projectid 
    if ratl_forminfo is not None:
        fields["cq:ratl_forminfo"] = ratl_forminfo 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if release_sdom is not None:
        fields["cq:Release_SDOM"] = release_sdom 
    if release_sdom_userid is not None:
        fields["cq:Release_SDOM_UserID"] = release_sdom_userid 
    if requester is not None:
        fields["cq:Requester"] = requester 
    if requesterfilter is not None:
        fields["cq:RequesterFilter"] = requesterfilter 
    if requestersearch is not None:
        fields["cq:RequesterSearch"] = requestersearch 
    if rotitle is not None:
        fields["cq:ROTitle"] = rotitle 
    if rotype is not None:
        fields["cq:ROType"] = rotype 
    if scmcomment is not None:
        fields["cq:SCMComment"] = scmcomment 
    if scmsystem is not None:
        fields["cq:SCMSystem"] = scmsystem 
    if scope is not None:
        fields["cq:Scope"] = scope 
    if selectassignee is not None:
        fields["cq:SelectAssignee"] = selectassignee 
    if shorttitle is not None:
        fields["oslc:shortTitle"] = shorttitle 
    if specificationfreeze is not None:
        fields["cq:SpecificationFreeze"] = specificationfreeze 
    if startdate is not None:
        fields["cq:StartDate"] = startdate 
    if state is not None:
        fields["cq:State"] = state 
    if status is not None:
        fields["oslc_cm:status"] = status 
    if subject is not None:
        fields["dcterms:subject"] = subject 
    if submitdate is not None:
        fields["cq:SubmitDate"] = submitdate 
    if submitter is not None:
        fields["cq:Submitter"] = submitter 
    if supplierrequestedbaseline is not None:
        fields["cq:SupplierRequestedBaseline"] = supplierrequestedbaseline 
    if suppressvalidation is not None:
        fields["cq:SuppressValidation"] = suppressvalidation 
    if suppressvalidationcomment is not None:
        fields["cq:SuppressValidationComment"] = suppressvalidationcomment 
    if swarchitecturedecision is not None:
        fields["cq:SwArchitectureDecision"] = swarchitecturedecision 
    if swtest_bft is not None:
        fields["cq:SWTest_BFT"] = swtest_bft 
    if swtest_com is not None:
        fields["cq:SWTest_COM"] = swtest_com 
    if swtest_cst is not None:
        fields["cq:SWTest_CST"] = swtest_cst 
    if swtest_eeprom is not None:
        fields["cq:SWTest_EEPROM"] = swtest_eeprom 
    if swtest_ft is not None:
        fields["cq:SWTest_FT"] = swtest_ft 
    if swtest_io is not None:
        fields["cq:SWTest_IO"] = swtest_io 
    if swtest_opt is not None:
        fields["cq:SWTest_OPT"] = swtest_opt 
    if swtest_ost is not None:
        fields["cq:SWTest_OST"] = swtest_ost 
    if swtest_pver_conf is not None:
        fields["cq:SWTest_PVER_Conf"] = swtest_pver_conf 
    if swtest_pver_i is not None:
        fields["cq:SWTest_PVER_I"] = swtest_pver_i 
    if swtest_pverf is not None:
        fields["cq:SWTest_PVERF"] = swtest_pverf 
    if swtest_pveri is not None:
        fields["cq:SWTest_PVERI"] = swtest_pveri 
    if swtest_robustness is not None:
        fields["cq:SWTest_Robustness"] = swtest_robustness 
    if swtest_srr is not None:
        fields["cq:SWTest_SRR"] = swtest_srr 
    if swtest_viva is not None:
        fields["cq:SWTest_VIVA"] = swtest_viva 
    if sync is not None:
        fields["cq:Sync"] = sync 
    if tags is not None:
        fields["cq:Tags"] = tags 
    if template is not None:
        fields["cq:Template"] = template 
    if tld is not None:
        fields["cq:TLD"] = tld 
    if toolversion is not None:
        fields["cq:ToolVersion"] = toolversion 
    if unduplicate_state is not None:
        fields["cq:unduplicate_state"] = unduplicate_state 
    if validationexceptcomment is not None:
        fields["cq:ValidationExceptComment"] = validationexceptcomment 
    if validationexceptions is not None:
        fields["cq:ValidationExceptions"] = validationexceptions 
    if version is not None:
        fields["cq:version"] = version 
    if workflowstatus is not None:
        fields["cq:WorkflowStatus"] = workflowstatus 
    

    fields["dcterms:type"] = "Release"
    return Release(**fields)