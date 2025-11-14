
from .base import Resource, QueryStatement, BaseRecord
from pydantic import ConfigDict, Field, field_validator
import typing
from datetime import datetime, timezone
from enum import Enum
import json


from typing import TYPE_CHECKING

if TYPE_CHECKING:

    from .historylog import Historylog

    from .rq1_roleuser import Rq1_Roleuser

    from .externallink import Externallink

    from .users import Users

    from .exchangeprotocol import Exchangeprotocol

    from .problem import Problem

    from .issue import Issue

    from .workitem import Workitem

    from .background import Background

    from .ratl_replicas import Ratl_Replicas

    from .release import Release

    from .contact import Contact



class Project(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    accountnumbers: typing.Optional[str] = Field(alias="cq:AccountNumbers", default=None)
    
    attachments: typing.Optional[Resource] = Field(alias="cq:Attachments", default=None)
    
    belongstopoolproject: typing.Optional[typing.Union[Resource, 'Project']] = Field(alias="cq:belongsToPoolProject", default=None)
    
    belongstoreferenceproject: typing.Optional[typing.Union[Resource, 'Project']] = Field(alias="cq:belongsToReferenceProject", default=None)
    
    bulkoperations: typing.Optional[str] = Field(alias="cq:BulkOperations", default=None)
    
    classification: typing.Optional[str] = Field(alias="cq:Classification", default=None)
    
    cq__Description: typing.Optional[str] = Field(alias="cq:Description", default=None)
    
    cq__Title: typing.Optional[str] = Field(alias="cq:Title", default=None)
    
    cq__Type: typing.Optional[str] = Field(alias="cq:Type", default=None)
    
    creator: typing.Optional[Resource] = Field(alias="dcterms:creator", default=None)
    
    customer: typing.Optional[str] = Field(alias="cq:Customer", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__description: typing.Optional[str] = Field(alias="dcterms:description", default=None)
    
    dcterms__title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    defaultdevelopmentmethod: typing.Optional[str] = Field(alias="cq:DefaultDevelopmentMethod", default=None)
    
    defaultplanningmethod: typing.Optional[str] = Field(alias="cq:DefaultPlanningMethod", default=None)
    
    defvalexceptcomment: typing.Optional[str] = Field(alias="cq:DefValExceptComment", default=None)
    
    defvalexceptions: typing.Optional[str] = Field(alias="cq:DefValExceptions", default=None)
    
    domain: typing.Optional[str] = Field(alias="cq:Domain", default=None)
    
    estimatedeffort: typing.Optional[int] = Field(alias="cq:EstimatedEffort", default=None)
    
    estimationcomment: typing.Optional[str] = Field(alias="cq:EstimationComment", default=None)
    
    external_id: typing.Optional[str] = Field(alias="cq:External_ID", default=None)
    
    externaldescription: typing.Optional[str] = Field(alias="cq:ExternalDescription", default=None)
    
    externalinformation: typing.Optional[str] = Field(alias="cq:ExternalInformation", default=None)
    
    externalprojectleader: typing.Optional[typing.Union[Resource, 'Contact']] = Field(alias="cq:ExternalProjectLeader", default=None)
    
    externalstate: typing.Optional[str] = Field(alias="cq:ExternalState", default=None)
    
    externaltitle: typing.Optional[str] = Field(alias="cq:ExternalTitle", default=None)
    
    generation: typing.Optional[str] = Field(alias="cq:Generation", default=None)
    
    generations: typing.Optional[str] = Field(alias="cq:Generations", default=None)
    
    hasbackground: typing.Optional[typing.Union[Resource, 'Background']] = Field(alias="cq:hasBackground", default=None)
    
    haschildren: typing.List[typing.Union[Resource, 'Project']] = Field(alias="cq:hasChildren", default_factory=lambda: [])
    
    hasexchangeprotocols: typing.List[typing.Union[Resource, 'Exchangeprotocol']] = Field(alias="cq:hasExchangeProtocols", default_factory=lambda: [])
    
    hasexternallinks: typing.List[typing.Union[Resource, 'Externallink']] = Field(alias="cq:hasExternalLinks", default_factory=lambda: [])
    
    hashistorylogs: typing.List[typing.Union[Resource, 'Historylog']] = Field(alias="cq:hasHistoryLogs", default_factory=lambda: [])
    
    hasissues: typing.List[typing.Union[Resource, 'Issue']] = Field(alias="cq:hasIssues", default_factory=lambda: [])
    
    hasparent: typing.Optional[typing.Union[Resource, 'Project']] = Field(alias="cq:hasParent", default=None)
    
    haspoolprojectmembers: typing.List[typing.Union[Resource, 'Project']] = Field(alias="cq:hasPoolProjectMembers", default_factory=lambda: [])
    
    hasproblems: typing.List[typing.Union[Resource, 'Problem']] = Field(alias="cq:hasProblems", default_factory=lambda: [])
    
    hasprojectmembers: typing.List[typing.Union[Resource, 'Rq1_Roleuser']] = Field(alias="cq:hasProjectMembers", default_factory=lambda: [])
    
    hasreferenceprjmembers: typing.List[typing.Union[Resource, 'Project']] = Field(alias="cq:hasReferencePrjMembers", default_factory=lambda: [])
    
    hasreleases: typing.List[typing.Union[Resource, 'Release']] = Field(alias="cq:hasReleases", default_factory=lambda: [])
    
    hasworkitems: typing.List[typing.Union[Resource, 'Workitem']] = Field(alias="cq:hasWorkitems", default_factory=lambda: [])
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    history_log: typing.Optional[str] = Field(alias="cq:History_Log", default=None)
    
    id: typing.Optional[str] = Field(alias="cq:id", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    internalcomment: typing.Optional[str] = Field(alias="cq:InternalComment", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    is_duplicate: typing.Optional[int] = Field(alias="cq:is_duplicate", default=None)
    
    ispoolproject: typing.Optional[typing.Literal["No", "Yes"]] = Field(alias="cq:IsPoolProject", default=None)
    
    isreferenceproject: typing.Optional[typing.Literal["No", "Yes"]] = Field(alias="cq:IsReferenceProject", default=None)
    
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
    
    products: typing.Optional[str] = Field(alias="cq:Products", default=None)
    
    projectleader: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:ProjectLeader", default=None)
    
    ratl_forminfo: typing.Optional[str] = Field(alias="cq:ratl_forminfo", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    responsibleatbosch: typing.Optional[str] = Field(alias="cq:ResponsibleAtBosch", default=None)
    
    rotitle: typing.Optional[str] = Field(alias="cq:ROTitle", default=None)
    
    rotype: typing.Optional[str] = Field(alias="cq:ROType", default=None)
    
    scope: typing.Optional[str] = Field(alias="cq:Scope", default=None)
    
    shorttitle: typing.Optional[str] = Field(alias="oslc:shortTitle", default=None)
    
    state: typing.Optional[str] = Field(alias="cq:State", default=None)
    
    status: typing.Optional[str] = Field(alias="oslc_cm:status", default=None)
    
    subject: typing.List[str] = Field(alias="dcterms:subject", default_factory=lambda: [])
    
    submitdate: typing.Optional[datetime] = Field(alias="cq:SubmitDate", default=None)
    
    submitter: typing.Optional[str] = Field(alias="cq:Submitter", default=None)
    
    swarchitecturedecision: typing.Optional[str] = Field(alias="cq:SwArchitectureDecision", default=None)
    
    synchronization: typing.Optional[str] = Field(alias="cq:Synchronization", default=None)
    
    tags: typing.Optional[str] = Field(alias="cq:Tags", default=None)
    
    tagstool: typing.Optional[str] = Field(alias="cq:TagsTool", default=None)
    
    templates: typing.Optional[str] = Field(alias="cq:Templates", default=None)
    
    tld: typing.Optional[str] = Field(alias="cq:TLD", default=None)
    
    toolversion: typing.Optional[str] = Field(alias="cq:ToolVersion", default=None)
    
    unduplicate_state: typing.Optional[str] = Field(alias="cq:unduplicate_state", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16777229"

    @classmethod
    def shape_name(cls) -> str:
        return "Project"

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
    


class ProjectProperty(Enum):
    accountnumbers = "cq:AccountNumbers"
    attachments = "cq:Attachments"
    belongstopoolproject = "cq:belongsToPoolProject"
    belongstoreferenceproject = "cq:belongsToReferenceProject"
    bulkoperations = "cq:BulkOperations"
    classification = "cq:Classification"
    cq__Description = "cq:Description"
    cq__Title = "cq:Title"
    cq__Type = "cq:Type"
    creator = "dcterms:creator"
    customer = "cq:Customer"
    dbid = "cq:dbid"
    dcterms__description = "dcterms:description"
    dcterms__title = "dcterms:title"
    dcterms__type = "dcterms:type"
    defaultdevelopmentmethod = "cq:DefaultDevelopmentMethod"
    defaultplanningmethod = "cq:DefaultPlanningMethod"
    defvalexceptcomment = "cq:DefValExceptComment"
    defvalexceptions = "cq:DefValExceptions"
    domain = "cq:Domain"
    estimatedeffort = "cq:EstimatedEffort"
    estimationcomment = "cq:EstimationComment"
    external_id = "cq:External_ID"
    externaldescription = "cq:ExternalDescription"
    externalinformation = "cq:ExternalInformation"
    externalprojectleader = "cq:ExternalProjectLeader"
    externalstate = "cq:ExternalState"
    externaltitle = "cq:ExternalTitle"
    generation = "cq:Generation"
    generations = "cq:Generations"
    hasbackground = "cq:hasBackground"
    haschildren = "cq:hasChildren"
    hasexchangeprotocols = "cq:hasExchangeProtocols"
    hasexternallinks = "cq:hasExternalLinks"
    hashistorylogs = "cq:hasHistoryLogs"
    hasissues = "cq:hasIssues"
    hasparent = "cq:hasParent"
    haspoolprojectmembers = "cq:hasPoolProjectMembers"
    hasproblems = "cq:hasProblems"
    hasprojectmembers = "cq:hasProjectMembers"
    hasreferenceprjmembers = "cq:hasReferencePrjMembers"
    hasreleases = "cq:hasReleases"
    hasworkitems = "cq:hasWorkitems"
    history = "cq:history"
    history_log = "cq:History_Log"
    id = "cq:id"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    internalcomment = "cq:InternalComment"
    is_active = "cq:is_active"
    is_duplicate = "cq:is_duplicate"
    ispoolproject = "cq:IsPoolProject"
    isreferenceproject = "cq:IsReferenceProject"
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
    products = "cq:Products"
    projectleader = "cq:ProjectLeader"
    ratl_forminfo = "cq:ratl_forminfo"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    responsibleatbosch = "cq:ResponsibleAtBosch"
    rotitle = "cq:ROTitle"
    rotype = "cq:ROType"
    scope = "cq:Scope"
    shorttitle = "oslc:shortTitle"
    state = "cq:State"
    status = "oslc_cm:status"
    subject = "dcterms:subject"
    submitdate = "cq:SubmitDate"
    submitter = "cq:Submitter"
    swarchitecturedecision = "cq:SwArchitectureDecision"
    synchronization = "cq:Synchronization"
    tags = "cq:Tags"
    tagstool = "cq:TagsTool"
    templates = "cq:Templates"
    tld = "cq:TLD"
    toolversion = "cq:ToolVersion"
    unduplicate_state = "cq:unduplicate_state"
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


def create_project_changeset(
    accountnumbers: typing.Optional[str] = None,
    attachments: typing.Optional[Resource] = None,
    belongstopoolproject: typing.Optional[typing.Union[Resource, 'Project']] = None,
    belongstoreferenceproject: typing.Optional[typing.Union[Resource, 'Project']] = None,
    bulkoperations: typing.Optional[str] = None,
    classification: typing.Optional[str] = None,
    cq__Description: typing.Optional[str] = None,
    cq__Title: typing.Optional[str] = None,
    cq__Type: typing.Optional[str] = None,
    creator: typing.Optional[Resource] = None,
    customer: typing.Optional[str] = None,
    dbid: typing.Optional[str] = None,
    dcterms__description: typing.Optional[str] = None,
    dcterms__title: typing.Optional[str] = None,
    defaultdevelopmentmethod: typing.Optional[str] = None,
    defaultplanningmethod: typing.Optional[str] = None,
    defvalexceptcomment: typing.Optional[str] = None,
    defvalexceptions: typing.Optional[str] = None,
    domain: typing.Optional[str] = None,
    estimatedeffort: typing.Optional[int] = None,
    estimationcomment: typing.Optional[str] = None,
    external_id: typing.Optional[str] = None,
    externaldescription: typing.Optional[str] = None,
    externalinformation: typing.Optional[str] = None,
    externalprojectleader: typing.Optional[typing.Union[Resource, 'Contact']] = None,
    externalstate: typing.Optional[str] = None,
    externaltitle: typing.Optional[str] = None,
    generation: typing.Optional[str] = None,
    generations: typing.Optional[str] = None,
    hasbackground: typing.Optional[typing.Union[Resource, 'Background']] = None,
    haschildren: typing.Optional[typing.List[typing.Union[Resource, 'Project']]] = None,
    hasexchangeprotocols: typing.Optional[typing.List[typing.Union[Resource, 'Exchangeprotocol']]] = None,
    hasexternallinks: typing.Optional[typing.List[typing.Union[Resource, 'Externallink']]] = None,
    hashistorylogs: typing.Optional[typing.List[typing.Union[Resource, 'Historylog']]] = None,
    hasissues: typing.Optional[typing.List[typing.Union[Resource, 'Issue']]] = None,
    hasparent: typing.Optional[typing.Union[Resource, 'Project']] = None,
    haspoolprojectmembers: typing.Optional[typing.List[typing.Union[Resource, 'Project']]] = None,
    hasproblems: typing.Optional[typing.List[typing.Union[Resource, 'Problem']]] = None,
    hasprojectmembers: typing.Optional[typing.List[typing.Union[Resource, 'Rq1_Roleuser']]] = None,
    hasreferenceprjmembers: typing.Optional[typing.List[typing.Union[Resource, 'Project']]] = None,
    hasreleases: typing.Optional[typing.List[typing.Union[Resource, 'Release']]] = None,
    hasworkitems: typing.Optional[typing.List[typing.Union[Resource, 'Workitem']]] = None,
    history: typing.Optional[Resource] = None,
    history_log: typing.Optional[str] = None,
    id: typing.Optional[str] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    internalcomment: typing.Optional[str] = None,
    is_active: typing.Optional[int] = None,
    is_duplicate: typing.Optional[int] = None,
    ispoolproject: typing.Optional[typing.Literal["No", "Yes"]] = None,
    isreferenceproject: typing.Optional[typing.Literal["No", "Yes"]] = None,
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
    products: typing.Optional[str] = None,
    projectleader: typing.Optional[typing.Union[Resource, 'Users']] = None,
    ratl_forminfo: typing.Optional[str] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    responsibleatbosch: typing.Optional[str] = None,
    rotitle: typing.Optional[str] = None,
    rotype: typing.Optional[str] = None,
    scope: typing.Optional[str] = None,
    shorttitle: typing.Optional[str] = None,
    state: typing.Optional[str] = None,
    status: typing.Optional[str] = None,
    subject: typing.Optional[typing.List[str]] = None,
    submitdate: typing.Optional[datetime] = None,
    submitter: typing.Optional[str] = None,
    swarchitecturedecision: typing.Optional[str] = None,
    synchronization: typing.Optional[str] = None,
    tags: typing.Optional[str] = None,
    tagstool: typing.Optional[str] = None,
    templates: typing.Optional[str] = None,
    tld: typing.Optional[str] = None,
    toolversion: typing.Optional[str] = None,
    unduplicate_state: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    
) -> Project:
    '''
    Create a Project changeset.
    '''
    fields = {}
    if accountnumbers is not None:
        fields["cq:AccountNumbers"] = accountnumbers 
    if attachments is not None:
        fields["cq:Attachments"] = attachments 
    if belongstopoolproject is not None:
        fields["cq:belongsToPoolProject"] = belongstopoolproject 
    if belongstoreferenceproject is not None:
        fields["cq:belongsToReferenceProject"] = belongstoreferenceproject 
    if bulkoperations is not None:
        fields["cq:BulkOperations"] = bulkoperations 
    if classification is not None:
        fields["cq:Classification"] = classification 
    if cq__Description is not None:
        fields["cq:Description"] = cq__Description 
    if cq__Title is not None:
        fields["cq:Title"] = cq__Title 
    if cq__Type is not None:
        fields["cq:Type"] = cq__Type 
    if creator is not None:
        fields["dcterms:creator"] = creator 
    if customer is not None:
        fields["cq:Customer"] = customer 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if dcterms__description is not None:
        fields["dcterms:description"] = dcterms__description 
    if dcterms__title is not None:
        fields["dcterms:title"] = dcterms__title 
    if defaultdevelopmentmethod is not None:
        fields["cq:DefaultDevelopmentMethod"] = defaultdevelopmentmethod 
    if defaultplanningmethod is not None:
        fields["cq:DefaultPlanningMethod"] = defaultplanningmethod 
    if defvalexceptcomment is not None:
        fields["cq:DefValExceptComment"] = defvalexceptcomment 
    if defvalexceptions is not None:
        fields["cq:DefValExceptions"] = defvalexceptions 
    if domain is not None:
        fields["cq:Domain"] = domain 
    if estimatedeffort is not None:
        fields["cq:EstimatedEffort"] = estimatedeffort 
    if estimationcomment is not None:
        fields["cq:EstimationComment"] = estimationcomment 
    if external_id is not None:
        fields["cq:External_ID"] = external_id 
    if externaldescription is not None:
        fields["cq:ExternalDescription"] = externaldescription 
    if externalinformation is not None:
        fields["cq:ExternalInformation"] = externalinformation 
    if externalprojectleader is not None:
        fields["cq:ExternalProjectLeader"] = externalprojectleader 
    if externalstate is not None:
        fields["cq:ExternalState"] = externalstate 
    if externaltitle is not None:
        fields["cq:ExternalTitle"] = externaltitle 
    if generation is not None:
        fields["cq:Generation"] = generation 
    if generations is not None:
        fields["cq:Generations"] = generations 
    if hasbackground is not None:
        fields["cq:hasBackground"] = hasbackground 
    if haschildren is not None:
        fields["cq:hasChildren"] = haschildren 
    if hasexchangeprotocols is not None:
        fields["cq:hasExchangeProtocols"] = hasexchangeprotocols 
    if hasexternallinks is not None:
        fields["cq:hasExternalLinks"] = hasexternallinks 
    if hashistorylogs is not None:
        fields["cq:hasHistoryLogs"] = hashistorylogs 
    if hasissues is not None:
        fields["cq:hasIssues"] = hasissues 
    if hasparent is not None:
        fields["cq:hasParent"] = hasparent 
    if haspoolprojectmembers is not None:
        fields["cq:hasPoolProjectMembers"] = haspoolprojectmembers 
    if hasproblems is not None:
        fields["cq:hasProblems"] = hasproblems 
    if hasprojectmembers is not None:
        fields["cq:hasProjectMembers"] = hasprojectmembers 
    if hasreferenceprjmembers is not None:
        fields["cq:hasReferencePrjMembers"] = hasreferenceprjmembers 
    if hasreleases is not None:
        fields["cq:hasReleases"] = hasreleases 
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
    if ispoolproject is not None:
        fields["cq:IsPoolProject"] = ispoolproject 
    if isreferenceproject is not None:
        fields["cq:IsReferenceProject"] = isreferenceproject 
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
    if products is not None:
        fields["cq:Products"] = products 
    if projectleader is not None:
        fields["cq:ProjectLeader"] = projectleader 
    if ratl_forminfo is not None:
        fields["cq:ratl_forminfo"] = ratl_forminfo 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if responsibleatbosch is not None:
        fields["cq:ResponsibleAtBosch"] = responsibleatbosch 
    if rotitle is not None:
        fields["cq:ROTitle"] = rotitle 
    if rotype is not None:
        fields["cq:ROType"] = rotype 
    if scope is not None:
        fields["cq:Scope"] = scope 
    if shorttitle is not None:
        fields["oslc:shortTitle"] = shorttitle 
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
    if swarchitecturedecision is not None:
        fields["cq:SwArchitectureDecision"] = swarchitecturedecision 
    if synchronization is not None:
        fields["cq:Synchronization"] = synchronization 
    if tags is not None:
        fields["cq:Tags"] = tags 
    if tagstool is not None:
        fields["cq:TagsTool"] = tagstool 
    if templates is not None:
        fields["cq:Templates"] = templates 
    if tld is not None:
        fields["cq:TLD"] = tld 
    if toolversion is not None:
        fields["cq:ToolVersion"] = toolversion 
    if unduplicate_state is not None:
        fields["cq:unduplicate_state"] = unduplicate_state 
    if version is not None:
        fields["cq:version"] = version 
    

    fields["dcterms:type"] = "Project"
    return Project(**fields)