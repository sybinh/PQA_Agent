
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

    from .issueissuemap import Issueissuemap

    from .externallink import Externallink

    from .attachmentmapping import Attachmentmapping

    from .project import Project

    from .users import Users

    from .problem import Problem

    from .issuereleasemap import Issuereleasemap

    from .workitem import Workitem

    from .background import Background

    from .ratl_replicas import Ratl_Replicas

    from .release import Release

    from .contact import Contact



class Issue(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    acceptancecriteria: typing.Optional[str] = Field(alias="cq:AcceptanceCriteria", default=None)
    
    accountnumbers: typing.Optional[str] = Field(alias="cq:AccountNumbers", default=None)
    
    adminattachments: typing.Optional[Resource] = Field(alias="cq:AdminAttachments", default=None)
    
    affectedissue: typing.Optional[typing.Union[Resource, 'Issue']] = Field(alias="cq:AffectedIssue", default=None)
    
    affectedissuefilter: typing.Optional[str] = Field(alias="cq:AffectedIssueFilter", default=None)
    
    affectedissuesearch: typing.Optional[str] = Field(alias="cq:AffectedIssueSearch", default=None)
    
    affectedreleases: typing.List[typing.Union[Resource, 'Release']] = Field(alias="cq:AffectedReleases", default_factory=lambda: [])
    
    alg2rv: typing.Optional[str] = Field(alias="cq:Alg2rv", default=None)
    
    algorithmstoreview: typing.Optional[str] = Field(alias="cq:AlgorithmsToReview", default=None)
    
    algorithmstoreviewcomment: typing.Optional[str] = Field(alias="cq:AlgorithmsToReviewComment", default=None)
    
    allocation: typing.Optional[str] = Field(alias="cq:Allocation", default=None)
    
    approval: typing.Optional[str] = Field(alias="cq:Approval", default=None)
    
    asilclassification: typing.Optional[str] = Field(alias="cq:ASILClassification", default=None)
    
    assignee: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:Assignee", default=None)
    
    assigneefilter: typing.Optional[str] = Field(alias="cq:AssigneeFilter", default=None)
    
    assigneesearch: typing.Optional[str] = Field(alias="cq:AssigneeSearch", default=None)
    
    attachments: typing.Optional[Resource] = Field(alias="cq:Attachments", default=None)
    
    belongstoproblem: typing.Optional[typing.Union[Resource, 'Problem']] = Field(alias="cq:belongsToProblem", default=None)
    
    belongstoproject: typing.Optional[typing.Union[Resource, 'Project']] = Field(alias="cq:belongsToProject", default=None)
    
    belongstoprojectfilter: typing.Optional[str] = Field(alias="cq:belongsToProjectFilter", default=None)
    
    belongstoprojectsearch: typing.Optional[str] = Field(alias="cq:belongsToProjectSearch", default=None)
    
    category: typing.Optional[str] = Field(alias="cq:Category", default=None)
    
    categoryexhaust: typing.Optional[str] = Field(alias="cq:CategoryExhaust", default=None)
    
    categoryexhaustcomment: typing.Optional[str] = Field(alias="cq:CategoryExhaustComment", default=None)
    
    classification: typing.Optional[str] = Field(alias="cq:Classification", default=None)
    
    codexsubcategory: typing.Optional[str] = Field(alias="cq:CodexSubCategory", default=None)
    
    collaboration: typing.Optional[str] = Field(alias="cq:Collaboration", default=None)
    
    commercialallowance: typing.Optional[int] = Field(alias="cq:CommercialAllowance", default=None)
    
    commercialamount: typing.Optional[str] = Field(alias="cq:CommercialAmount", default=None)
    
    commercialamountconfirmed: typing.Optional[str] = Field(alias="cq:CommercialAmountConfirmed", default=None)
    
    commercialamountdetails: typing.Optional[str] = Field(alias="cq:CommercialAmountDetails", default=None)
    
    commercialassignee: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:CommercialAssignee", default=None)
    
    commercialassigneefilter: typing.Optional[str] = Field(alias="cq:CommercialAssigneeFilter", default=None)
    
    commercialassigneesearch: typing.Optional[str] = Field(alias="cq:CommercialAssigneeSearch", default=None)
    
    commercialattachments: typing.Optional[Resource] = Field(alias="cq:CommercialAttachments", default=None)
    
    commercialcategory: typing.Optional[str] = Field(alias="cq:CommercialCategory", default=None)
    
    commercialclassification: typing.Optional[str] = Field(alias="cq:CommercialClassification", default=None)
    
    commercialcomment: typing.Optional[str] = Field(alias="cq:CommercialComment", default=None)
    
    commercialconversation: typing.Optional[str] = Field(alias="cq:CommercialConversation", default=None)
    
    commercialnote: typing.Optional[str] = Field(alias="cq:CommercialNote", default=None)
    
    commercialorigsolution: typing.Optional[int] = Field(alias="cq:CommercialOrigSolution", default=None)
    
    commercialorigsolutionacc: typing.Optional[str] = Field(alias="cq:CommercialOrigSolutionAcc", default=None)
    
    commercialquotationid: typing.Optional[str] = Field(alias="cq:CommercialQuotationId", default=None)
    
    commercialquotationreq: typing.Optional[str] = Field(alias="cq:CommercialQuotationReq", default=None)
    
    commercialsolution: typing.Optional[str] = Field(alias="cq:CommercialSolution", default=None)
    
    cq__Description: typing.Optional[str] = Field(alias="cq:Description", default=None)
    
    cq__Title: typing.Optional[str] = Field(alias="cq:Title", default=None)
    
    cq__Type: typing.Optional[str] = Field(alias="cq:Type", default=None)
    
    createcommercial: typing.Optional[typing.Literal["No", "Yes"]] = Field(alias="cq:CreateCommercial", default=None)
    
    creationmode: typing.Optional[str] = Field(alias="cq:CreationMode", default=None)
    
    creator: typing.Optional[Resource] = Field(alias="dcterms:creator", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__description: typing.Optional[str] = Field(alias="dcterms:description", default=None)
    
    dcterms__title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    defectclassification: typing.Optional[str] = Field(alias="cq:DefectClassification", default=None)
    
    defectdetectiondate: typing.Optional[datetime] = Field(alias="cq:DefectDetectionDate", default=None)
    
    defectdetectionlocation: typing.Optional[str] = Field(alias="cq:DefectDetectionLocation", default=None)
    
    defectdetectionorga: typing.Optional[str] = Field(alias="cq:DefectDetectionOrga", default=None)
    
    defectdetectionprocess: typing.Optional[str] = Field(alias="cq:DefectDetectionProcess", default=None)
    
    defectinjectiondate: typing.Optional[datetime] = Field(alias="cq:DefectInjectionDate", default=None)
    
    defectinjectionorga: typing.Optional[str] = Field(alias="cq:DefectInjectionOrga", default=None)
    
    defectiveworkproducttype: typing.Optional[str] = Field(alias="cq:DefectiveWorkproductType", default=None)
    
    defectrootcauseclass: typing.Optional[str] = Field(alias="cq:DefectRootCauseClass", default=None)
    
    developmentmethod: typing.Optional[str] = Field(alias="cq:DevelopmentMethod", default=None)
    
    domain: typing.Optional[str] = Field(alias="cq:Domain", default=None)
    
    drbfm: typing.Optional[str] = Field(alias="cq:DRBFM", default=None)
    
    enablebulkoperations: typing.Optional[str] = Field(alias="cq:EnableBulkOperations", default=None)
    
    estimatedeffort: typing.Optional[int] = Field(alias="cq:EstimatedEffort", default=None)
    
    estimationcomment: typing.Optional[str] = Field(alias="cq:EstimationComment", default=None)
    
    external_id: typing.Optional[str] = Field(alias="cq:External_ID", default=None)
    
    externalassignee: typing.Optional[typing.Union[Resource, 'Contact']] = Field(alias="cq:ExternalAssignee", default=None)
    
    externalcomment: typing.Optional[str] = Field(alias="cq:ExternalComment", default=None)
    
    externalconversation: typing.Optional[str] = Field(alias="cq:ExternalConversation", default=None)
    
    externaldescription: typing.Optional[str] = Field(alias="cq:ExternalDescription", default=None)
    
    externalexchangedattach: typing.Optional[str] = Field(alias="cq:ExternalExchangedAttach", default=None)
    
    externalexchangeformat: typing.Optional[str] = Field(alias="cq:ExternalExchangeFormat", default=None)
    
    externalexchangeworkflow: typing.Optional[str] = Field(alias="cq:ExternalExchangeWorkflow", default=None)
    
    externalhistory: typing.Optional[str] = Field(alias="cq:ExternalHistory", default=None)
    
    externalinformation: typing.Optional[str] = Field(alias="cq:ExternalInformation", default=None)
    
    externallastexporteddate: typing.Optional[datetime] = Field(alias="cq:ExternalLastExportedDate", default=None)
    
    externallastimporteddate: typing.Optional[datetime] = Field(alias="cq:ExternalLastImportedDate", default=None)
    
    externalmilestones: typing.Optional[str] = Field(alias="cq:ExternalMilestones", default=None)
    
    externalnextstate: typing.Optional[str] = Field(alias="cq:ExternalNextState", default=None)
    
    externalorganisation: typing.Optional[str] = Field(alias="cq:ExternalOrganisation", default=None)
    
    externalproject: typing.Optional[str] = Field(alias="cq:ExternalProject", default=None)
    
    externalreview: typing.Optional[str] = Field(alias="cq:ExternalReview", default=None)
    
    externalstate: typing.Optional[str] = Field(alias="cq:ExternalState", default=None)
    
    externalstate_parallel1: typing.Optional[str] = Field(alias="cq:ExternalState_Parallel1", default=None)
    
    externalstate_parallel2: typing.Optional[str] = Field(alias="cq:ExternalState_Parallel2", default=None)
    
    externalsubmitter: typing.Optional[typing.Union[Resource, 'Contact']] = Field(alias="cq:ExternalSubmitter", default=None)
    
    externaltags: typing.Optional[str] = Field(alias="cq:ExternalTags", default=None)
    
    externaltitle: typing.Optional[str] = Field(alias="cq:ExternalTitle", default=None)
    
    externalupdateversion: typing.Optional[str] = Field(alias="cq:ExternalUpdateVersion", default=None)
    
    externalupdateversionexp: typing.Optional[str] = Field(alias="cq:ExternalUpdateVersionExp", default=None)
    
    externalupdateversionimp: typing.Optional[str] = Field(alias="cq:ExternalUpdateVersionImp", default=None)
    
    fmea: typing.Optional[str] = Field(alias="cq:FMEA", default=None)
    
    fmearelevance: typing.Optional[str] = Field(alias="cq:FMEARelevance", default=None)
    
    forms: typing.Optional[str] = Field(alias="cq:Forms", default=None)
    
    fulltextsearchtitle: typing.Optional[str] = Field(alias="cq:FullTextSearchTitle", default=None)
    
    hasattachmentmappings: typing.List[typing.Union[Resource, 'Attachmentmapping']] = Field(alias="cq:hasAttachmentMappings", default_factory=lambda: [])
    
    hasbackground: typing.Optional[typing.Union[Resource, 'Background']] = Field(alias="cq:hasBackground", default=None)
    
    haschildren: typing.List[typing.Union[Resource, 'Issue']] = Field(alias="cq:hasChildren", default_factory=lambda: [])
    
    hascommercialdata: typing.Optional[typing.Union[Resource, 'Commercial']] = Field(alias="cq:hasCommercialData", default=None)
    
    hasexternallinks: typing.List[typing.Union[Resource, 'Externallink']] = Field(alias="cq:hasExternalLinks", default_factory=lambda: [])
    
    hashistorylogs: typing.List[typing.Union[Resource, 'Historylog']] = Field(alias="cq:hasHistoryLogs", default_factory=lambda: [])
    
    hasmappedissue1: typing.List[typing.Union[Resource, 'Issueissuemap']] = Field(alias="cq:hasMappedIssue1", default_factory=lambda: [])
    
    hasmappedissue2: typing.List[typing.Union[Resource, 'Issueissuemap']] = Field(alias="cq:hasMappedIssue2", default_factory=lambda: [])
    
    hasmappedreleases: typing.List[typing.Union[Resource, 'Issuereleasemap']] = Field(alias="cq:hasMappedReleases", default_factory=lambda: [])
    
    hasparent: typing.Optional[typing.Union[Resource, 'Issue']] = Field(alias="cq:hasParent", default=None)
    
    haspredecessor: typing.Optional[typing.Union[Resource, 'Issue']] = Field(alias="cq:hasPredecessor", default=None)
    
    haspredecessorfilter: typing.Optional[str] = Field(alias="cq:hasPredecessorFilter", default=None)
    
    haspredecessorsearch: typing.Optional[str] = Field(alias="cq:hasPredecessorSearch", default=None)
    
    hassuccessor: typing.List[typing.Union[Resource, 'Issue']] = Field(alias="cq:hasSuccessor", default_factory=lambda: [])
    
    hasworkitems: typing.List[typing.Union[Resource, 'Workitem']] = Field(alias="cq:hasWorkitems", default_factory=lambda: [])
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    history_log: typing.Optional[str] = Field(alias="cq:History_Log", default=None)
    
    id: typing.Optional[str] = Field(alias="cq:id", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    internalcomment: typing.Optional[str] = Field(alias="cq:InternalComment", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    is_duplicate: typing.Optional[int] = Field(alias="cq:is_duplicate", default=None)
    
    isaffectedbydefectissue: typing.List[typing.Union[Resource, 'Issue']] = Field(alias="cq:isAffectedByDefectIssue", default_factory=lambda: [])
    
    lastlifecyclestate: typing.Optional[str] = Field(alias="cq:LastLifeCycleState", default=None)
    
    lastmodifieddate: typing.Optional[datetime] = Field(alias="cq:LastModifiedDate", default=None)
    
    leanlessonslearned: typing.Optional[str] = Field(alias="cq:LeanLessonsLearned", default=None)
    
    lifecyclestate: typing.Optional[str] = Field(alias="cq:LifeCycleState", default=None)
    
    lifecyclestatecomment: typing.Optional[str] = Field(alias="cq:LifeCycleStateComment", default=None)
    
    links: typing.Optional[str] = Field(alias="cq:Links", default=None)
    
    lll5why: typing.Optional[str] = Field(alias="cq:LLL5Why", default=None)
    
    lll5whyinjectionrootcause: typing.Optional[str] = Field(alias="cq:LLL5WhyInjectionRootCause", default=None)
    
    lll5whynondetectrootcause: typing.Optional[str] = Field(alias="cq:LLL5WhyNonDetectRootCause", default=None)
    
    llldetaileddescription: typing.Optional[str] = Field(alias="cq:LLLDetailedDescription", default=None)
    
    lllinjectionrootcause: typing.Optional[str] = Field(alias="cq:LLLInjectionRootCause", default=None)
    
    lllmeasures: typing.Optional[str] = Field(alias="cq:LLLMeasures", default=None)
    
    lllnondetectionrootcause: typing.Optional[str] = Field(alias="cq:LLLNonDetectionRootCause", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    milestones: typing.Optional[str] = Field(alias="cq:Milestones", default=None)
    
    note_entry: typing.Optional[str] = Field(alias="cq:Note_Entry", default=None)
    
    notes_log: typing.Optional[str] = Field(alias="cq:Notes_Log", default=None)
    
    occurrence: typing.Optional[str] = Field(alias="cq:Occurrence", default=None)
    
    oemcollaborationmodel: typing.Optional[str] = Field(alias="cq:OEMCollaborationModel", default=None)
    
    oemproposaladopted: typing.Optional[typing.Literal["No", "Yes"]] = Field(alias="cq:OEMProposalAdopted", default=None)
    
    operationcontext: typing.Optional[str] = Field(alias="cq:OperationContext", default=None)
    
    operationmode: typing.Optional[str] = Field(alias="cq:OperationMode", default=None)
    
    oslccommand: typing.Optional[str] = Field(alias="cq:oslcCommand", default=None)
    
    priority: typing.Optional[str] = Field(alias="cq:Priority", default=None)
    
    processtailoring: typing.Optional[str] = Field(alias="cq:ProcessTailoring", default=None)
    
    product: typing.Optional[str] = Field(alias="cq:Product", default=None)
    
    productionrelevance: typing.Optional[str] = Field(alias="cq:ProductionRelevance", default=None)
    
    projectid: typing.Optional[str] = Field(alias="cq:ProjectId", default=None)
    
    radardefectclassification: typing.Optional[str] = Field(alias="cq:RADARDefectClassification", default=None)
    
    radarpifid: typing.Optional[str] = Field(alias="cq:RADARPIFID", default=None)
    
    ratl_forminfo: typing.Optional[str] = Field(alias="cq:ratl_forminfo", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    relateddefectclass: typing.Optional[str] = Field(alias="cq:RelatedDefectClass", default=None)
    
    relateddefectid: typing.Optional[str] = Field(alias="cq:RelatedDefectID", default=None)
    
    relatedid: typing.Optional[str] = Field(alias="cq:RelatedID", default=None)
    
    relatedsystem: typing.Optional[str] = Field(alias="cq:RelatedSystem", default=None)
    
    requester: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:Requester", default=None)
    
    requesterfilter: typing.Optional[str] = Field(alias="cq:RequesterFilter", default=None)
    
    requestersearch: typing.Optional[str] = Field(alias="cq:RequesterSearch", default=None)
    
    requirementsreview: typing.Optional[str] = Field(alias="cq:RequirementsReview", default=None)
    
    rotitle: typing.Optional[str] = Field(alias="cq:ROTitle", default=None)
    
    rotype: typing.Optional[str] = Field(alias="cq:ROType", default=None)
    
    safetyrelevance: typing.Optional[str] = Field(alias="cq:SafetyRelevance", default=None)
    
    scope: typing.Optional[str] = Field(alias="cq:Scope", default=None)
    
    selectassignee: typing.Optional[str] = Field(alias="cq:SelectAssignee", default=None)
    
    severity: typing.Optional[str] = Field(alias="cq:Severity", default=None)
    
    shorttitle: typing.Optional[str] = Field(alias="oslc:shortTitle", default=None)
    
    specificationreview: typing.Optional[str] = Field(alias="cq:SpecificationReview", default=None)
    
    state: typing.Optional[str] = Field(alias="cq:State", default=None)
    
    status: typing.Optional[str] = Field(alias="oslc_cm:status", default=None)
    
    subject: typing.List[str] = Field(alias="dcterms:subject", default_factory=lambda: [])
    
    submitdate: typing.Optional[datetime] = Field(alias="cq:SubmitDate", default=None)
    
    submitter: typing.Optional[str] = Field(alias="cq:Submitter", default=None)
    
    suppressvalidation: typing.Optional[str] = Field(alias="cq:SuppressValidation", default=None)
    
    suppressvalidationcomment: typing.Optional[str] = Field(alias="cq:SuppressValidationComment", default=None)
    
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
        return "16777231"

    @classmethod
    def shape_name(cls) -> str:
        return "Issue"

    @field_validator('*', mode="before")
    @classmethod
    def empty_str_to_none(cls, v):
        if v == '':
            return None
        return v

    
    @field_validator('instanceshape', 'history', 'attachments', 'adminattachments', 'commercialattachments', mode="before")
    @classmethod
    def list_to_resource(cls, v):
        if isinstance(v, list):
            return v[0]
        return v
    


class IssueProperty(Enum):
    acceptancecriteria = "cq:AcceptanceCriteria"
    accountnumbers = "cq:AccountNumbers"
    adminattachments = "cq:AdminAttachments"
    affectedissue = "cq:AffectedIssue"
    affectedissuefilter = "cq:AffectedIssueFilter"
    affectedissuesearch = "cq:AffectedIssueSearch"
    affectedreleases = "cq:AffectedReleases"
    alg2rv = "cq:Alg2rv"
    algorithmstoreview = "cq:AlgorithmsToReview"
    algorithmstoreviewcomment = "cq:AlgorithmsToReviewComment"
    allocation = "cq:Allocation"
    approval = "cq:Approval"
    asilclassification = "cq:ASILClassification"
    assignee = "cq:Assignee"
    assigneefilter = "cq:AssigneeFilter"
    assigneesearch = "cq:AssigneeSearch"
    attachments = "cq:Attachments"
    belongstoproblem = "cq:belongsToProblem"
    belongstoproject = "cq:belongsToProject"
    belongstoprojectfilter = "cq:belongsToProjectFilter"
    belongstoprojectsearch = "cq:belongsToProjectSearch"
    category = "cq:Category"
    categoryexhaust = "cq:CategoryExhaust"
    categoryexhaustcomment = "cq:CategoryExhaustComment"
    classification = "cq:Classification"
    codexsubcategory = "cq:CodexSubCategory"
    collaboration = "cq:Collaboration"
    commercialallowance = "cq:CommercialAllowance"
    commercialamount = "cq:CommercialAmount"
    commercialamountconfirmed = "cq:CommercialAmountConfirmed"
    commercialamountdetails = "cq:CommercialAmountDetails"
    commercialassignee = "cq:CommercialAssignee"
    commercialassigneefilter = "cq:CommercialAssigneeFilter"
    commercialassigneesearch = "cq:CommercialAssigneeSearch"
    commercialattachments = "cq:CommercialAttachments"
    commercialcategory = "cq:CommercialCategory"
    commercialclassification = "cq:CommercialClassification"
    commercialcomment = "cq:CommercialComment"
    commercialconversation = "cq:CommercialConversation"
    commercialnote = "cq:CommercialNote"
    commercialorigsolution = "cq:CommercialOrigSolution"
    commercialorigsolutionacc = "cq:CommercialOrigSolutionAcc"
    commercialquotationid = "cq:CommercialQuotationId"
    commercialquotationreq = "cq:CommercialQuotationReq"
    commercialsolution = "cq:CommercialSolution"
    cq__Description = "cq:Description"
    cq__Title = "cq:Title"
    cq__Type = "cq:Type"
    createcommercial = "cq:CreateCommercial"
    creationmode = "cq:CreationMode"
    creator = "dcterms:creator"
    dbid = "cq:dbid"
    dcterms__description = "dcterms:description"
    dcterms__title = "dcterms:title"
    dcterms__type = "dcterms:type"
    defectclassification = "cq:DefectClassification"
    defectdetectiondate = "cq:DefectDetectionDate"
    defectdetectionlocation = "cq:DefectDetectionLocation"
    defectdetectionorga = "cq:DefectDetectionOrga"
    defectdetectionprocess = "cq:DefectDetectionProcess"
    defectinjectiondate = "cq:DefectInjectionDate"
    defectinjectionorga = "cq:DefectInjectionOrga"
    defectiveworkproducttype = "cq:DefectiveWorkproductType"
    defectrootcauseclass = "cq:DefectRootCauseClass"
    developmentmethod = "cq:DevelopmentMethod"
    domain = "cq:Domain"
    drbfm = "cq:DRBFM"
    enablebulkoperations = "cq:EnableBulkOperations"
    estimatedeffort = "cq:EstimatedEffort"
    estimationcomment = "cq:EstimationComment"
    external_id = "cq:External_ID"
    externalassignee = "cq:ExternalAssignee"
    externalcomment = "cq:ExternalComment"
    externalconversation = "cq:ExternalConversation"
    externaldescription = "cq:ExternalDescription"
    externalexchangedattach = "cq:ExternalExchangedAttach"
    externalexchangeformat = "cq:ExternalExchangeFormat"
    externalexchangeworkflow = "cq:ExternalExchangeWorkflow"
    externalhistory = "cq:ExternalHistory"
    externalinformation = "cq:ExternalInformation"
    externallastexporteddate = "cq:ExternalLastExportedDate"
    externallastimporteddate = "cq:ExternalLastImportedDate"
    externalmilestones = "cq:ExternalMilestones"
    externalnextstate = "cq:ExternalNextState"
    externalorganisation = "cq:ExternalOrganisation"
    externalproject = "cq:ExternalProject"
    externalreview = "cq:ExternalReview"
    externalstate = "cq:ExternalState"
    externalstate_parallel1 = "cq:ExternalState_Parallel1"
    externalstate_parallel2 = "cq:ExternalState_Parallel2"
    externalsubmitter = "cq:ExternalSubmitter"
    externaltags = "cq:ExternalTags"
    externaltitle = "cq:ExternalTitle"
    externalupdateversion = "cq:ExternalUpdateVersion"
    externalupdateversionexp = "cq:ExternalUpdateVersionExp"
    externalupdateversionimp = "cq:ExternalUpdateVersionImp"
    fmea = "cq:FMEA"
    fmearelevance = "cq:FMEARelevance"
    forms = "cq:Forms"
    fulltextsearchtitle = "cq:FullTextSearchTitle"
    hasattachmentmappings = "cq:hasAttachmentMappings"
    hasbackground = "cq:hasBackground"
    haschildren = "cq:hasChildren"
    hascommercialdata = "cq:hasCommercialData"
    hasexternallinks = "cq:hasExternalLinks"
    hashistorylogs = "cq:hasHistoryLogs"
    hasmappedissue1 = "cq:hasMappedIssue1"
    hasmappedissue2 = "cq:hasMappedIssue2"
    hasmappedreleases = "cq:hasMappedReleases"
    hasparent = "cq:hasParent"
    haspredecessor = "cq:hasPredecessor"
    haspredecessorfilter = "cq:hasPredecessorFilter"
    haspredecessorsearch = "cq:hasPredecessorSearch"
    hassuccessor = "cq:hasSuccessor"
    hasworkitems = "cq:hasWorkitems"
    history = "cq:history"
    history_log = "cq:History_Log"
    id = "cq:id"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    internalcomment = "cq:InternalComment"
    is_active = "cq:is_active"
    is_duplicate = "cq:is_duplicate"
    isaffectedbydefectissue = "cq:isAffectedByDefectIssue"
    lastlifecyclestate = "cq:LastLifeCycleState"
    lastmodifieddate = "cq:LastModifiedDate"
    leanlessonslearned = "cq:LeanLessonsLearned"
    lifecyclestate = "cq:LifeCycleState"
    lifecyclestatecomment = "cq:LifeCycleStateComment"
    links = "cq:Links"
    lll5why = "cq:LLL5Why"
    lll5whyinjectionrootcause = "cq:LLL5WhyInjectionRootCause"
    lll5whynondetectrootcause = "cq:LLL5WhyNonDetectRootCause"
    llldetaileddescription = "cq:LLLDetailedDescription"
    lllinjectionrootcause = "cq:LLLInjectionRootCause"
    lllmeasures = "cq:LLLMeasures"
    lllnondetectionrootcause = "cq:LLLNonDetectionRootCause"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    milestones = "cq:Milestones"
    note_entry = "cq:Note_Entry"
    notes_log = "cq:Notes_Log"
    occurrence = "cq:Occurrence"
    oemcollaborationmodel = "cq:OEMCollaborationModel"
    oemproposaladopted = "cq:OEMProposalAdopted"
    operationcontext = "cq:OperationContext"
    operationmode = "cq:OperationMode"
    oslccommand = "cq:oslcCommand"
    priority = "cq:Priority"
    processtailoring = "cq:ProcessTailoring"
    product = "cq:Product"
    productionrelevance = "cq:ProductionRelevance"
    projectid = "cq:ProjectId"
    radardefectclassification = "cq:RADARDefectClassification"
    radarpifid = "cq:RADARPIFID"
    ratl_forminfo = "cq:ratl_forminfo"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    relateddefectclass = "cq:RelatedDefectClass"
    relateddefectid = "cq:RelatedDefectID"
    relatedid = "cq:RelatedID"
    relatedsystem = "cq:RelatedSystem"
    requester = "cq:Requester"
    requesterfilter = "cq:RequesterFilter"
    requestersearch = "cq:RequesterSearch"
    requirementsreview = "cq:RequirementsReview"
    rotitle = "cq:ROTitle"
    rotype = "cq:ROType"
    safetyrelevance = "cq:SafetyRelevance"
    scope = "cq:Scope"
    selectassignee = "cq:SelectAssignee"
    severity = "cq:Severity"
    shorttitle = "oslc:shortTitle"
    specificationreview = "cq:SpecificationReview"
    state = "cq:State"
    status = "oslc_cm:status"
    subject = "dcterms:subject"
    submitdate = "cq:SubmitDate"
    submitter = "cq:Submitter"
    suppressvalidation = "cq:SuppressValidation"
    suppressvalidationcomment = "cq:SuppressValidationComment"
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


def create_issue_changeset(
    acceptancecriteria: typing.Optional[str] = None,
    accountnumbers: typing.Optional[str] = None,
    adminattachments: typing.Optional[Resource] = None,
    affectedissue: typing.Optional[typing.Union[Resource, 'Issue']] = None,
    affectedissuefilter: typing.Optional[str] = None,
    affectedissuesearch: typing.Optional[str] = None,
    affectedreleases: typing.Optional[typing.List[typing.Union[Resource, 'Release']]] = None,
    alg2rv: typing.Optional[str] = None,
    algorithmstoreview: typing.Optional[str] = None,
    algorithmstoreviewcomment: typing.Optional[str] = None,
    allocation: typing.Optional[str] = None,
    approval: typing.Optional[str] = None,
    asilclassification: typing.Optional[str] = None,
    assignee: typing.Optional[typing.Union[Resource, 'Users']] = None,
    assigneefilter: typing.Optional[str] = None,
    assigneesearch: typing.Optional[str] = None,
    attachments: typing.Optional[Resource] = None,
    belongstoproblem: typing.Optional[typing.Union[Resource, 'Problem']] = None,
    belongstoproject: typing.Optional[typing.Union[Resource, 'Project']] = None,
    belongstoprojectfilter: typing.Optional[str] = None,
    belongstoprojectsearch: typing.Optional[str] = None,
    category: typing.Optional[str] = None,
    categoryexhaust: typing.Optional[str] = None,
    categoryexhaustcomment: typing.Optional[str] = None,
    classification: typing.Optional[str] = None,
    codexsubcategory: typing.Optional[str] = None,
    collaboration: typing.Optional[str] = None,
    commercialallowance: typing.Optional[int] = None,
    commercialamount: typing.Optional[str] = None,
    commercialamountconfirmed: typing.Optional[str] = None,
    commercialamountdetails: typing.Optional[str] = None,
    commercialassignee: typing.Optional[typing.Union[Resource, 'Users']] = None,
    commercialassigneefilter: typing.Optional[str] = None,
    commercialassigneesearch: typing.Optional[str] = None,
    commercialattachments: typing.Optional[Resource] = None,
    commercialcategory: typing.Optional[str] = None,
    commercialclassification: typing.Optional[str] = None,
    commercialcomment: typing.Optional[str] = None,
    commercialconversation: typing.Optional[str] = None,
    commercialnote: typing.Optional[str] = None,
    commercialorigsolution: typing.Optional[int] = None,
    commercialorigsolutionacc: typing.Optional[str] = None,
    commercialquotationid: typing.Optional[str] = None,
    commercialquotationreq: typing.Optional[str] = None,
    commercialsolution: typing.Optional[str] = None,
    cq__Description: typing.Optional[str] = None,
    cq__Title: typing.Optional[str] = None,
    cq__Type: typing.Optional[str] = None,
    createcommercial: typing.Optional[typing.Literal["No", "Yes"]] = None,
    creationmode: typing.Optional[str] = None,
    creator: typing.Optional[Resource] = None,
    dbid: typing.Optional[str] = None,
    dcterms__description: typing.Optional[str] = None,
    dcterms__title: typing.Optional[str] = None,
    defectclassification: typing.Optional[str] = None,
    defectdetectiondate: typing.Optional[datetime] = None,
    defectdetectionlocation: typing.Optional[str] = None,
    defectdetectionorga: typing.Optional[str] = None,
    defectdetectionprocess: typing.Optional[str] = None,
    defectinjectiondate: typing.Optional[datetime] = None,
    defectinjectionorga: typing.Optional[str] = None,
    defectiveworkproducttype: typing.Optional[str] = None,
    defectrootcauseclass: typing.Optional[str] = None,
    developmentmethod: typing.Optional[str] = None,
    domain: typing.Optional[str] = None,
    drbfm: typing.Optional[str] = None,
    enablebulkoperations: typing.Optional[str] = None,
    estimatedeffort: typing.Optional[int] = None,
    estimationcomment: typing.Optional[str] = None,
    external_id: typing.Optional[str] = None,
    externalassignee: typing.Optional[typing.Union[Resource, 'Contact']] = None,
    externalcomment: typing.Optional[str] = None,
    externalconversation: typing.Optional[str] = None,
    externaldescription: typing.Optional[str] = None,
    externalexchangedattach: typing.Optional[str] = None,
    externalexchangeformat: typing.Optional[str] = None,
    externalexchangeworkflow: typing.Optional[str] = None,
    externalhistory: typing.Optional[str] = None,
    externalinformation: typing.Optional[str] = None,
    externallastexporteddate: typing.Optional[datetime] = None,
    externallastimporteddate: typing.Optional[datetime] = None,
    externalmilestones: typing.Optional[str] = None,
    externalnextstate: typing.Optional[str] = None,
    externalorganisation: typing.Optional[str] = None,
    externalproject: typing.Optional[str] = None,
    externalreview: typing.Optional[str] = None,
    externalstate: typing.Optional[str] = None,
    externalstate_parallel1: typing.Optional[str] = None,
    externalstate_parallel2: typing.Optional[str] = None,
    externalsubmitter: typing.Optional[typing.Union[Resource, 'Contact']] = None,
    externaltags: typing.Optional[str] = None,
    externaltitle: typing.Optional[str] = None,
    externalupdateversion: typing.Optional[str] = None,
    externalupdateversionexp: typing.Optional[str] = None,
    externalupdateversionimp: typing.Optional[str] = None,
    fmea: typing.Optional[str] = None,
    fmearelevance: typing.Optional[str] = None,
    forms: typing.Optional[str] = None,
    fulltextsearchtitle: typing.Optional[str] = None,
    hasattachmentmappings: typing.Optional[typing.List[typing.Union[Resource, 'Attachmentmapping']]] = None,
    hasbackground: typing.Optional[typing.Union[Resource, 'Background']] = None,
    haschildren: typing.Optional[typing.List[typing.Union[Resource, 'Issue']]] = None,
    hascommercialdata: typing.Optional[typing.Union[Resource, 'Commercial']] = None,
    hasexternallinks: typing.Optional[typing.List[typing.Union[Resource, 'Externallink']]] = None,
    hashistorylogs: typing.Optional[typing.List[typing.Union[Resource, 'Historylog']]] = None,
    hasmappedissue1: typing.Optional[typing.List[typing.Union[Resource, 'Issueissuemap']]] = None,
    hasmappedissue2: typing.Optional[typing.List[typing.Union[Resource, 'Issueissuemap']]] = None,
    hasmappedreleases: typing.Optional[typing.List[typing.Union[Resource, 'Issuereleasemap']]] = None,
    hasparent: typing.Optional[typing.Union[Resource, 'Issue']] = None,
    haspredecessor: typing.Optional[typing.Union[Resource, 'Issue']] = None,
    haspredecessorfilter: typing.Optional[str] = None,
    haspredecessorsearch: typing.Optional[str] = None,
    hassuccessor: typing.Optional[typing.List[typing.Union[Resource, 'Issue']]] = None,
    hasworkitems: typing.Optional[typing.List[typing.Union[Resource, 'Workitem']]] = None,
    history: typing.Optional[Resource] = None,
    history_log: typing.Optional[str] = None,
    id: typing.Optional[str] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    internalcomment: typing.Optional[str] = None,
    is_active: typing.Optional[int] = None,
    is_duplicate: typing.Optional[int] = None,
    isaffectedbydefectissue: typing.Optional[typing.List[typing.Union[Resource, 'Issue']]] = None,
    lastlifecyclestate: typing.Optional[str] = None,
    lastmodifieddate: typing.Optional[datetime] = None,
    leanlessonslearned: typing.Optional[str] = None,
    lifecyclestate: typing.Optional[str] = None,
    lifecyclestatecomment: typing.Optional[str] = None,
    links: typing.Optional[str] = None,
    lll5why: typing.Optional[str] = None,
    lll5whyinjectionrootcause: typing.Optional[str] = None,
    lll5whynondetectrootcause: typing.Optional[str] = None,
    llldetaileddescription: typing.Optional[str] = None,
    lllinjectionrootcause: typing.Optional[str] = None,
    lllmeasures: typing.Optional[str] = None,
    lllnondetectionrootcause: typing.Optional[str] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    milestones: typing.Optional[str] = None,
    note_entry: typing.Optional[str] = None,
    notes_log: typing.Optional[str] = None,
    occurrence: typing.Optional[str] = None,
    oemcollaborationmodel: typing.Optional[str] = None,
    oemproposaladopted: typing.Optional[typing.Literal["No", "Yes"]] = None,
    operationcontext: typing.Optional[str] = None,
    operationmode: typing.Optional[str] = None,
    oslccommand: typing.Optional[str] = None,
    priority: typing.Optional[str] = None,
    processtailoring: typing.Optional[str] = None,
    product: typing.Optional[str] = None,
    productionrelevance: typing.Optional[str] = None,
    projectid: typing.Optional[str] = None,
    radardefectclassification: typing.Optional[str] = None,
    radarpifid: typing.Optional[str] = None,
    ratl_forminfo: typing.Optional[str] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    relateddefectclass: typing.Optional[str] = None,
    relateddefectid: typing.Optional[str] = None,
    relatedid: typing.Optional[str] = None,
    relatedsystem: typing.Optional[str] = None,
    requester: typing.Optional[typing.Union[Resource, 'Users']] = None,
    requesterfilter: typing.Optional[str] = None,
    requestersearch: typing.Optional[str] = None,
    requirementsreview: typing.Optional[str] = None,
    rotitle: typing.Optional[str] = None,
    rotype: typing.Optional[str] = None,
    safetyrelevance: typing.Optional[str] = None,
    scope: typing.Optional[str] = None,
    selectassignee: typing.Optional[str] = None,
    severity: typing.Optional[str] = None,
    shorttitle: typing.Optional[str] = None,
    specificationreview: typing.Optional[str] = None,
    state: typing.Optional[str] = None,
    status: typing.Optional[str] = None,
    subject: typing.Optional[typing.List[str]] = None,
    submitdate: typing.Optional[datetime] = None,
    submitter: typing.Optional[str] = None,
    suppressvalidation: typing.Optional[str] = None,
    suppressvalidationcomment: typing.Optional[str] = None,
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
    
) -> Issue:
    '''
    Create a Issue changeset.
    '''
    fields = {}
    if acceptancecriteria is not None:
        fields["cq:AcceptanceCriteria"] = acceptancecriteria 
    if accountnumbers is not None:
        fields["cq:AccountNumbers"] = accountnumbers 
    if adminattachments is not None:
        fields["cq:AdminAttachments"] = adminattachments 
    if affectedissue is not None:
        fields["cq:AffectedIssue"] = affectedissue 
    if affectedissuefilter is not None:
        fields["cq:AffectedIssueFilter"] = affectedissuefilter 
    if affectedissuesearch is not None:
        fields["cq:AffectedIssueSearch"] = affectedissuesearch 
    if affectedreleases is not None:
        fields["cq:AffectedReleases"] = affectedreleases 
    if alg2rv is not None:
        fields["cq:Alg2rv"] = alg2rv 
    if algorithmstoreview is not None:
        fields["cq:AlgorithmsToReview"] = algorithmstoreview 
    if algorithmstoreviewcomment is not None:
        fields["cq:AlgorithmsToReviewComment"] = algorithmstoreviewcomment 
    if allocation is not None:
        fields["cq:Allocation"] = allocation 
    if approval is not None:
        fields["cq:Approval"] = approval 
    if asilclassification is not None:
        fields["cq:ASILClassification"] = asilclassification 
    if assignee is not None:
        fields["cq:Assignee"] = assignee 
    if assigneefilter is not None:
        fields["cq:AssigneeFilter"] = assigneefilter 
    if assigneesearch is not None:
        fields["cq:AssigneeSearch"] = assigneesearch 
    if attachments is not None:
        fields["cq:Attachments"] = attachments 
    if belongstoproblem is not None:
        fields["cq:belongsToProblem"] = belongstoproblem 
    if belongstoproject is not None:
        fields["cq:belongsToProject"] = belongstoproject 
    if belongstoprojectfilter is not None:
        fields["cq:belongsToProjectFilter"] = belongstoprojectfilter 
    if belongstoprojectsearch is not None:
        fields["cq:belongsToProjectSearch"] = belongstoprojectsearch 
    if category is not None:
        fields["cq:Category"] = category 
    if categoryexhaust is not None:
        fields["cq:CategoryExhaust"] = categoryexhaust 
    if categoryexhaustcomment is not None:
        fields["cq:CategoryExhaustComment"] = categoryexhaustcomment 
    if classification is not None:
        fields["cq:Classification"] = classification 
    if codexsubcategory is not None:
        fields["cq:CodexSubCategory"] = codexsubcategory 
    if collaboration is not None:
        fields["cq:Collaboration"] = collaboration 
    if commercialallowance is not None:
        fields["cq:CommercialAllowance"] = commercialallowance 
    if commercialamount is not None:
        fields["cq:CommercialAmount"] = commercialamount 
    if commercialamountconfirmed is not None:
        fields["cq:CommercialAmountConfirmed"] = commercialamountconfirmed 
    if commercialamountdetails is not None:
        fields["cq:CommercialAmountDetails"] = commercialamountdetails 
    if commercialassignee is not None:
        fields["cq:CommercialAssignee"] = commercialassignee 
    if commercialassigneefilter is not None:
        fields["cq:CommercialAssigneeFilter"] = commercialassigneefilter 
    if commercialassigneesearch is not None:
        fields["cq:CommercialAssigneeSearch"] = commercialassigneesearch 
    if commercialattachments is not None:
        fields["cq:CommercialAttachments"] = commercialattachments 
    if commercialcategory is not None:
        fields["cq:CommercialCategory"] = commercialcategory 
    if commercialclassification is not None:
        fields["cq:CommercialClassification"] = commercialclassification 
    if commercialcomment is not None:
        fields["cq:CommercialComment"] = commercialcomment 
    if commercialconversation is not None:
        fields["cq:CommercialConversation"] = commercialconversation 
    if commercialnote is not None:
        fields["cq:CommercialNote"] = commercialnote 
    if commercialorigsolution is not None:
        fields["cq:CommercialOrigSolution"] = commercialorigsolution 
    if commercialorigsolutionacc is not None:
        fields["cq:CommercialOrigSolutionAcc"] = commercialorigsolutionacc 
    if commercialquotationid is not None:
        fields["cq:CommercialQuotationId"] = commercialquotationid 
    if commercialquotationreq is not None:
        fields["cq:CommercialQuotationReq"] = commercialquotationreq 
    if commercialsolution is not None:
        fields["cq:CommercialSolution"] = commercialsolution 
    if cq__Description is not None:
        fields["cq:Description"] = cq__Description 
    if cq__Title is not None:
        fields["cq:Title"] = cq__Title 
    if cq__Type is not None:
        fields["cq:Type"] = cq__Type 
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
    if dcterms__title is not None:
        fields["dcterms:title"] = dcterms__title 
    if defectclassification is not None:
        fields["cq:DefectClassification"] = defectclassification 
    if defectdetectiondate is not None:
        fields["cq:DefectDetectionDate"] = defectdetectiondate 
    if defectdetectionlocation is not None:
        fields["cq:DefectDetectionLocation"] = defectdetectionlocation 
    if defectdetectionorga is not None:
        fields["cq:DefectDetectionOrga"] = defectdetectionorga 
    if defectdetectionprocess is not None:
        fields["cq:DefectDetectionProcess"] = defectdetectionprocess 
    if defectinjectiondate is not None:
        fields["cq:DefectInjectionDate"] = defectinjectiondate 
    if defectinjectionorga is not None:
        fields["cq:DefectInjectionOrga"] = defectinjectionorga 
    if defectiveworkproducttype is not None:
        fields["cq:DefectiveWorkproductType"] = defectiveworkproducttype 
    if defectrootcauseclass is not None:
        fields["cq:DefectRootCauseClass"] = defectrootcauseclass 
    if developmentmethod is not None:
        fields["cq:DevelopmentMethod"] = developmentmethod 
    if domain is not None:
        fields["cq:Domain"] = domain 
    if drbfm is not None:
        fields["cq:DRBFM"] = drbfm 
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
    if externalconversation is not None:
        fields["cq:ExternalConversation"] = externalconversation 
    if externaldescription is not None:
        fields["cq:ExternalDescription"] = externaldescription 
    if externalexchangedattach is not None:
        fields["cq:ExternalExchangedAttach"] = externalexchangedattach 
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
    if externalmilestones is not None:
        fields["cq:ExternalMilestones"] = externalmilestones 
    if externalnextstate is not None:
        fields["cq:ExternalNextState"] = externalnextstate 
    if externalorganisation is not None:
        fields["cq:ExternalOrganisation"] = externalorganisation 
    if externalproject is not None:
        fields["cq:ExternalProject"] = externalproject 
    if externalreview is not None:
        fields["cq:ExternalReview"] = externalreview 
    if externalstate is not None:
        fields["cq:ExternalState"] = externalstate 
    if externalstate_parallel1 is not None:
        fields["cq:ExternalState_Parallel1"] = externalstate_parallel1 
    if externalstate_parallel2 is not None:
        fields["cq:ExternalState_Parallel2"] = externalstate_parallel2 
    if externalsubmitter is not None:
        fields["cq:ExternalSubmitter"] = externalsubmitter 
    if externaltags is not None:
        fields["cq:ExternalTags"] = externaltags 
    if externaltitle is not None:
        fields["cq:ExternalTitle"] = externaltitle 
    if externalupdateversion is not None:
        fields["cq:ExternalUpdateVersion"] = externalupdateversion 
    if externalupdateversionexp is not None:
        fields["cq:ExternalUpdateVersionExp"] = externalupdateversionexp 
    if externalupdateversionimp is not None:
        fields["cq:ExternalUpdateVersionImp"] = externalupdateversionimp 
    if fmea is not None:
        fields["cq:FMEA"] = fmea 
    if fmearelevance is not None:
        fields["cq:FMEARelevance"] = fmearelevance 
    if forms is not None:
        fields["cq:Forms"] = forms 
    if fulltextsearchtitle is not None:
        fields["cq:FullTextSearchTitle"] = fulltextsearchtitle 
    if hasattachmentmappings is not None:
        fields["cq:hasAttachmentMappings"] = hasattachmentmappings 
    if hasbackground is not None:
        fields["cq:hasBackground"] = hasbackground 
    if haschildren is not None:
        fields["cq:hasChildren"] = haschildren 
    if hascommercialdata is not None:
        fields["cq:hasCommercialData"] = hascommercialdata 
    if hasexternallinks is not None:
        fields["cq:hasExternalLinks"] = hasexternallinks 
    if hashistorylogs is not None:
        fields["cq:hasHistoryLogs"] = hashistorylogs 
    if hasmappedissue1 is not None:
        fields["cq:hasMappedIssue1"] = hasmappedissue1 
    if hasmappedissue2 is not None:
        fields["cq:hasMappedIssue2"] = hasmappedissue2 
    if hasmappedreleases is not None:
        fields["cq:hasMappedReleases"] = hasmappedreleases 
    if hasparent is not None:
        fields["cq:hasParent"] = hasparent 
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
    if instanceshape is not None:
        fields["oslc:instanceShape"] = instanceshape 
    if internalcomment is not None:
        fields["cq:InternalComment"] = internalcomment 
    if is_active is not None:
        fields["cq:is_active"] = is_active 
    if is_duplicate is not None:
        fields["cq:is_duplicate"] = is_duplicate 
    if isaffectedbydefectissue is not None:
        fields["cq:isAffectedByDefectIssue"] = isaffectedbydefectissue 
    if lastlifecyclestate is not None:
        fields["cq:LastLifeCycleState"] = lastlifecyclestate 
    if lastmodifieddate is not None:
        fields["cq:LastModifiedDate"] = lastmodifieddate 
    if leanlessonslearned is not None:
        fields["cq:LeanLessonsLearned"] = leanlessonslearned 
    if lifecyclestate is not None:
        fields["cq:LifeCycleState"] = lifecyclestate 
    if lifecyclestatecomment is not None:
        fields["cq:LifeCycleStateComment"] = lifecyclestatecomment 
    if links is not None:
        fields["cq:Links"] = links 
    if lll5why is not None:
        fields["cq:LLL5Why"] = lll5why 
    if lll5whyinjectionrootcause is not None:
        fields["cq:LLL5WhyInjectionRootCause"] = lll5whyinjectionrootcause 
    if lll5whynondetectrootcause is not None:
        fields["cq:LLL5WhyNonDetectRootCause"] = lll5whynondetectrootcause 
    if llldetaileddescription is not None:
        fields["cq:LLLDetailedDescription"] = llldetaileddescription 
    if lllinjectionrootcause is not None:
        fields["cq:LLLInjectionRootCause"] = lllinjectionrootcause 
    if lllmeasures is not None:
        fields["cq:LLLMeasures"] = lllmeasures 
    if lllnondetectionrootcause is not None:
        fields["cq:LLLNonDetectionRootCause"] = lllnondetectionrootcause 
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
    if occurrence is not None:
        fields["cq:Occurrence"] = occurrence 
    if oemcollaborationmodel is not None:
        fields["cq:OEMCollaborationModel"] = oemcollaborationmodel 
    if oemproposaladopted is not None:
        fields["cq:OEMProposalAdopted"] = oemproposaladopted 
    if operationcontext is not None:
        fields["cq:OperationContext"] = operationcontext 
    if operationmode is not None:
        fields["cq:OperationMode"] = operationmode 
    if oslccommand is not None:
        fields["cq:oslcCommand"] = oslccommand 
    if priority is not None:
        fields["cq:Priority"] = priority 
    if processtailoring is not None:
        fields["cq:ProcessTailoring"] = processtailoring 
    if product is not None:
        fields["cq:Product"] = product 
    if productionrelevance is not None:
        fields["cq:ProductionRelevance"] = productionrelevance 
    if projectid is not None:
        fields["cq:ProjectId"] = projectid 
    if radardefectclassification is not None:
        fields["cq:RADARDefectClassification"] = radardefectclassification 
    if radarpifid is not None:
        fields["cq:RADARPIFID"] = radarpifid 
    if ratl_forminfo is not None:
        fields["cq:ratl_forminfo"] = ratl_forminfo 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if relateddefectclass is not None:
        fields["cq:RelatedDefectClass"] = relateddefectclass 
    if relateddefectid is not None:
        fields["cq:RelatedDefectID"] = relateddefectid 
    if relatedid is not None:
        fields["cq:RelatedID"] = relatedid 
    if relatedsystem is not None:
        fields["cq:RelatedSystem"] = relatedsystem 
    if requester is not None:
        fields["cq:Requester"] = requester 
    if requesterfilter is not None:
        fields["cq:RequesterFilter"] = requesterfilter 
    if requestersearch is not None:
        fields["cq:RequesterSearch"] = requestersearch 
    if requirementsreview is not None:
        fields["cq:RequirementsReview"] = requirementsreview 
    if rotitle is not None:
        fields["cq:ROTitle"] = rotitle 
    if rotype is not None:
        fields["cq:ROType"] = rotype 
    if safetyrelevance is not None:
        fields["cq:SafetyRelevance"] = safetyrelevance 
    if scope is not None:
        fields["cq:Scope"] = scope 
    if selectassignee is not None:
        fields["cq:SelectAssignee"] = selectassignee 
    if severity is not None:
        fields["cq:Severity"] = severity 
    if shorttitle is not None:
        fields["oslc:shortTitle"] = shorttitle 
    if specificationreview is not None:
        fields["cq:SpecificationReview"] = specificationreview 
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
    if suppressvalidation is not None:
        fields["cq:SuppressValidation"] = suppressvalidation 
    if suppressvalidationcomment is not None:
        fields["cq:SuppressValidationComment"] = suppressvalidationcomment 
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
    

    fields["dcterms:type"] = "Issue"
    return Issue(**fields)