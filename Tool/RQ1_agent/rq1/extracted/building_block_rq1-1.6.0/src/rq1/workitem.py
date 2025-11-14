
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

    from .problem import Problem

    from .issue import Issue

    from .background import Background

    from .ratl_replicas import Ratl_Replicas

    from .release import Release



class Workitem(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    accountnumbers: typing.Optional[str] = Field(alias="cq:AccountNumbers", default=None)
    
    actualdate: typing.Optional[datetime] = Field(alias="cq:ActualDate", default=None)
    
    assignee: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:Assignee", default=None)
    
    assigneefilter: typing.Optional[str] = Field(alias="cq:AssigneeFilter", default=None)
    
    assigneesearch: typing.Optional[str] = Field(alias="cq:AssigneeSearch", default=None)
    
    attachments: typing.Optional[Resource] = Field(alias="cq:Attachments", default=None)
    
    belongstoissue: typing.Optional[typing.Union[Resource, 'Issue']] = Field(alias="cq:belongsToIssue", default=None)
    
    belongstoissuefilter: typing.Optional[str] = Field(alias="cq:belongsToIssueFilter", default=None)
    
    belongstoissuesearch: typing.Optional[str] = Field(alias="cq:belongsToIssueSearch", default=None)
    
    belongstoproblem: typing.Optional[typing.Union[Resource, 'Problem']] = Field(alias="cq:belongsToProblem", default=None)
    
    belongstoproblemfilter: typing.Optional[str] = Field(alias="cq:belongsToProblemFilter", default=None)
    
    belongstoproblemsearch: typing.Optional[str] = Field(alias="cq:belongsToProblemSearch", default=None)
    
    belongstoproject: typing.Optional[typing.Union[Resource, 'Project']] = Field(alias="cq:belongsToProject", default=None)
    
    belongstoprojectfilter: typing.Optional[str] = Field(alias="cq:belongsToProjectFilter", default=None)
    
    belongstoprojectsearch: typing.Optional[str] = Field(alias="cq:belongsToProjectSearch", default=None)
    
    belongstorelease: typing.Optional[typing.Union[Resource, 'Release']] = Field(alias="cq:belongsToRelease", default=None)
    
    belongstoreleasefilter: typing.Optional[str] = Field(alias="cq:belongsToReleaseFilter", default=None)
    
    belongstoreleasesearch: typing.Optional[str] = Field(alias="cq:belongsToReleaseSearch", default=None)
    
    category: typing.Optional[str] = Field(alias="cq:Category", default=None)
    
    cq__Description: typing.Optional[str] = Field(alias="cq:Description", default=None)
    
    cq__Title: typing.Optional[str] = Field(alias="cq:Title", default=None)
    
    cq__Type: typing.Optional[str] = Field(alias="cq:Type", default=None)
    
    creationmode: typing.Optional[str] = Field(alias="cq:CreationMode", default=None)
    
    creator: typing.Optional[Resource] = Field(alias="dcterms:creator", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__description: typing.Optional[str] = Field(alias="dcterms:description", default=None)
    
    dcterms__title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    dependsonworkitems: typing.List[typing.Union[Resource, 'Workitem']] = Field(alias="cq:dependsOnWorkitems", default_factory=lambda: [])
    
    domain: typing.Optional[str] = Field(alias="cq:Domain", default=None)
    
    estimatedeffort: typing.Optional[int] = Field(alias="cq:EstimatedEffort", default=None)
    
    estimationcomment: typing.Optional[str] = Field(alias="cq:EstimationComment", default=None)
    
    forms: typing.Optional[str] = Field(alias="cq:Forms", default=None)
    
    fulltextsearchtitle: typing.Optional[str] = Field(alias="cq:FullTextSearchTitle", default=None)
    
    hasbackground: typing.Optional[typing.Union[Resource, 'Background']] = Field(alias="cq:hasBackground", default=None)
    
    hasdependentworkitems: typing.List[typing.Union[Resource, 'Workitem']] = Field(alias="cq:hasDependentWorkitems", default_factory=lambda: [])
    
    hasexternallinks: typing.List[typing.Union[Resource, 'Externallink']] = Field(alias="cq:hasExternalLinks", default_factory=lambda: [])
    
    hashistorylogs: typing.List[typing.Union[Resource, 'Historylog']] = Field(alias="cq:hasHistoryLogs", default_factory=lambda: [])
    
    haspredecessors: typing.List[typing.Union[Resource, 'Workitem']] = Field(alias="cq:hasPredecessors", default_factory=lambda: [])
    
    hassuccessors: typing.List[typing.Union[Resource, 'Workitem']] = Field(alias="cq:hasSuccessors", default_factory=lambda: [])
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    history_log: typing.Optional[str] = Field(alias="cq:History_Log", default=None)
    
    id: typing.Optional[str] = Field(alias="cq:id", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    internalcomment: typing.Optional[str] = Field(alias="cq:InternalComment", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    is_duplicate: typing.Optional[int] = Field(alias="cq:is_duplicate", default=None)
    
    lastlifecyclestate: typing.Optional[str] = Field(alias="cq:LastLifeCycleState", default=None)
    
    lastmodifieddate: typing.Optional[datetime] = Field(alias="cq:LastModifiedDate", default=None)
    
    lifecyclestate: typing.Optional[str] = Field(alias="cq:LifeCycleState", default=None)
    
    lifecyclestatecomment: typing.Optional[str] = Field(alias="cq:LifeCycleStateComment", default=None)
    
    links: typing.Optional[str] = Field(alias="cq:Links", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    note_entry: typing.Optional[str] = Field(alias="cq:Note_Entry", default=None)
    
    notes_log: typing.Optional[str] = Field(alias="cq:Notes_Log", default=None)
    
    operationcontext: typing.Optional[str] = Field(alias="cq:OperationContext", default=None)
    
    operationmode: typing.Optional[str] = Field(alias="cq:OperationMode", default=None)
    
    oslccommand: typing.Optional[str] = Field(alias="cq:oslcCommand", default=None)
    
    planneddate: typing.Optional[datetime] = Field(alias="cq:PlannedDate", default=None)
    
    planningmethod: typing.Optional[str] = Field(alias="cq:PlanningMethod", default=None)
    
    priority: typing.Optional[str] = Field(alias="cq:Priority", default=None)
    
    product: typing.Optional[str] = Field(alias="cq:Product", default=None)
    
    ratl_forminfo: typing.Optional[str] = Field(alias="cq:ratl_forminfo", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    requester: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:Requester", default=None)
    
    requesterfilter: typing.Optional[str] = Field(alias="cq:RequesterFilter", default=None)
    
    requestersearch: typing.Optional[str] = Field(alias="cq:RequesterSearch", default=None)
    
    rotitle: typing.Optional[str] = Field(alias="cq:ROTitle", default=None)
    
    rotype: typing.Optional[str] = Field(alias="cq:ROType", default=None)
    
    selectassignee: typing.Optional[str] = Field(alias="cq:SelectAssignee", default=None)
    
    shorttitle: typing.Optional[str] = Field(alias="oslc:shortTitle", default=None)
    
    startdate: typing.Optional[datetime] = Field(alias="cq:StartDate", default=None)
    
    state: typing.Optional[str] = Field(alias="cq:State", default=None)
    
    status: typing.Optional[str] = Field(alias="oslc_cm:status", default=None)
    
    subcategory: typing.Optional[str] = Field(alias="cq:SubCategory", default=None)
    
    subject: typing.List[str] = Field(alias="dcterms:subject", default_factory=lambda: [])
    
    submitdate: typing.Optional[datetime] = Field(alias="cq:SubmitDate", default=None)
    
    submitter: typing.Optional[str] = Field(alias="cq:Submitter", default=None)
    
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
        return "16777232"

    @classmethod
    def shape_name(cls) -> str:
        return "Workitem"

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
    


class WorkitemProperty(Enum):
    accountnumbers = "cq:AccountNumbers"
    actualdate = "cq:ActualDate"
    assignee = "cq:Assignee"
    assigneefilter = "cq:AssigneeFilter"
    assigneesearch = "cq:AssigneeSearch"
    attachments = "cq:Attachments"
    belongstoissue = "cq:belongsToIssue"
    belongstoissuefilter = "cq:belongsToIssueFilter"
    belongstoissuesearch = "cq:belongsToIssueSearch"
    belongstoproblem = "cq:belongsToProblem"
    belongstoproblemfilter = "cq:belongsToProblemFilter"
    belongstoproblemsearch = "cq:belongsToProblemSearch"
    belongstoproject = "cq:belongsToProject"
    belongstoprojectfilter = "cq:belongsToProjectFilter"
    belongstoprojectsearch = "cq:belongsToProjectSearch"
    belongstorelease = "cq:belongsToRelease"
    belongstoreleasefilter = "cq:belongsToReleaseFilter"
    belongstoreleasesearch = "cq:belongsToReleaseSearch"
    category = "cq:Category"
    cq__Description = "cq:Description"
    cq__Title = "cq:Title"
    cq__Type = "cq:Type"
    creationmode = "cq:CreationMode"
    creator = "dcterms:creator"
    dbid = "cq:dbid"
    dcterms__description = "dcterms:description"
    dcterms__title = "dcterms:title"
    dcterms__type = "dcterms:type"
    dependsonworkitems = "cq:dependsOnWorkitems"
    domain = "cq:Domain"
    estimatedeffort = "cq:EstimatedEffort"
    estimationcomment = "cq:EstimationComment"
    forms = "cq:Forms"
    fulltextsearchtitle = "cq:FullTextSearchTitle"
    hasbackground = "cq:hasBackground"
    hasdependentworkitems = "cq:hasDependentWorkitems"
    hasexternallinks = "cq:hasExternalLinks"
    hashistorylogs = "cq:hasHistoryLogs"
    haspredecessors = "cq:hasPredecessors"
    hassuccessors = "cq:hasSuccessors"
    history = "cq:history"
    history_log = "cq:History_Log"
    id = "cq:id"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    internalcomment = "cq:InternalComment"
    is_active = "cq:is_active"
    is_duplicate = "cq:is_duplicate"
    lastlifecyclestate = "cq:LastLifeCycleState"
    lastmodifieddate = "cq:LastModifiedDate"
    lifecyclestate = "cq:LifeCycleState"
    lifecyclestatecomment = "cq:LifeCycleStateComment"
    links = "cq:Links"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    note_entry = "cq:Note_Entry"
    notes_log = "cq:Notes_Log"
    operationcontext = "cq:OperationContext"
    operationmode = "cq:OperationMode"
    oslccommand = "cq:oslcCommand"
    planneddate = "cq:PlannedDate"
    planningmethod = "cq:PlanningMethod"
    priority = "cq:Priority"
    product = "cq:Product"
    ratl_forminfo = "cq:ratl_forminfo"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    requester = "cq:Requester"
    requesterfilter = "cq:RequesterFilter"
    requestersearch = "cq:RequesterSearch"
    rotitle = "cq:ROTitle"
    rotype = "cq:ROType"
    selectassignee = "cq:SelectAssignee"
    shorttitle = "oslc:shortTitle"
    startdate = "cq:StartDate"
    state = "cq:State"
    status = "oslc_cm:status"
    subcategory = "cq:SubCategory"
    subject = "dcterms:subject"
    submitdate = "cq:SubmitDate"
    submitter = "cq:Submitter"
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


def create_workitem_changeset(
    accountnumbers: typing.Optional[str] = None,
    actualdate: typing.Optional[datetime] = None,
    assignee: typing.Optional[typing.Union[Resource, 'Users']] = None,
    assigneefilter: typing.Optional[str] = None,
    assigneesearch: typing.Optional[str] = None,
    attachments: typing.Optional[Resource] = None,
    belongstoissue: typing.Optional[typing.Union[Resource, 'Issue']] = None,
    belongstoissuefilter: typing.Optional[str] = None,
    belongstoissuesearch: typing.Optional[str] = None,
    belongstoproblem: typing.Optional[typing.Union[Resource, 'Problem']] = None,
    belongstoproblemfilter: typing.Optional[str] = None,
    belongstoproblemsearch: typing.Optional[str] = None,
    belongstoproject: typing.Optional[typing.Union[Resource, 'Project']] = None,
    belongstoprojectfilter: typing.Optional[str] = None,
    belongstoprojectsearch: typing.Optional[str] = None,
    belongstorelease: typing.Optional[typing.Union[Resource, 'Release']] = None,
    belongstoreleasefilter: typing.Optional[str] = None,
    belongstoreleasesearch: typing.Optional[str] = None,
    category: typing.Optional[str] = None,
    cq__Description: typing.Optional[str] = None,
    cq__Title: typing.Optional[str] = None,
    cq__Type: typing.Optional[str] = None,
    creationmode: typing.Optional[str] = None,
    creator: typing.Optional[Resource] = None,
    dbid: typing.Optional[str] = None,
    dcterms__description: typing.Optional[str] = None,
    dcterms__title: typing.Optional[str] = None,
    dependsonworkitems: typing.Optional[typing.List[typing.Union[Resource, 'Workitem']]] = None,
    domain: typing.Optional[str] = None,
    estimatedeffort: typing.Optional[int] = None,
    estimationcomment: typing.Optional[str] = None,
    forms: typing.Optional[str] = None,
    fulltextsearchtitle: typing.Optional[str] = None,
    hasbackground: typing.Optional[typing.Union[Resource, 'Background']] = None,
    hasdependentworkitems: typing.Optional[typing.List[typing.Union[Resource, 'Workitem']]] = None,
    hasexternallinks: typing.Optional[typing.List[typing.Union[Resource, 'Externallink']]] = None,
    hashistorylogs: typing.Optional[typing.List[typing.Union[Resource, 'Historylog']]] = None,
    haspredecessors: typing.Optional[typing.List[typing.Union[Resource, 'Workitem']]] = None,
    hassuccessors: typing.Optional[typing.List[typing.Union[Resource, 'Workitem']]] = None,
    history: typing.Optional[Resource] = None,
    history_log: typing.Optional[str] = None,
    id: typing.Optional[str] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    internalcomment: typing.Optional[str] = None,
    is_active: typing.Optional[int] = None,
    is_duplicate: typing.Optional[int] = None,
    lastlifecyclestate: typing.Optional[str] = None,
    lastmodifieddate: typing.Optional[datetime] = None,
    lifecyclestate: typing.Optional[str] = None,
    lifecyclestatecomment: typing.Optional[str] = None,
    links: typing.Optional[str] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    note_entry: typing.Optional[str] = None,
    notes_log: typing.Optional[str] = None,
    operationcontext: typing.Optional[str] = None,
    operationmode: typing.Optional[str] = None,
    oslccommand: typing.Optional[str] = None,
    planneddate: typing.Optional[datetime] = None,
    planningmethod: typing.Optional[str] = None,
    priority: typing.Optional[str] = None,
    product: typing.Optional[str] = None,
    ratl_forminfo: typing.Optional[str] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    requester: typing.Optional[typing.Union[Resource, 'Users']] = None,
    requesterfilter: typing.Optional[str] = None,
    requestersearch: typing.Optional[str] = None,
    rotitle: typing.Optional[str] = None,
    rotype: typing.Optional[str] = None,
    selectassignee: typing.Optional[str] = None,
    shorttitle: typing.Optional[str] = None,
    startdate: typing.Optional[datetime] = None,
    state: typing.Optional[str] = None,
    status: typing.Optional[str] = None,
    subcategory: typing.Optional[str] = None,
    subject: typing.Optional[typing.List[str]] = None,
    submitdate: typing.Optional[datetime] = None,
    submitter: typing.Optional[str] = None,
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
    
) -> Workitem:
    '''
    Create a Workitem changeset.
    '''
    fields = {}
    if accountnumbers is not None:
        fields["cq:AccountNumbers"] = accountnumbers 
    if actualdate is not None:
        fields["cq:ActualDate"] = actualdate 
    if assignee is not None:
        fields["cq:Assignee"] = assignee 
    if assigneefilter is not None:
        fields["cq:AssigneeFilter"] = assigneefilter 
    if assigneesearch is not None:
        fields["cq:AssigneeSearch"] = assigneesearch 
    if attachments is not None:
        fields["cq:Attachments"] = attachments 
    if belongstoissue is not None:
        fields["cq:belongsToIssue"] = belongstoissue 
    if belongstoissuefilter is not None:
        fields["cq:belongsToIssueFilter"] = belongstoissuefilter 
    if belongstoissuesearch is not None:
        fields["cq:belongsToIssueSearch"] = belongstoissuesearch 
    if belongstoproblem is not None:
        fields["cq:belongsToProblem"] = belongstoproblem 
    if belongstoproblemfilter is not None:
        fields["cq:belongsToProblemFilter"] = belongstoproblemfilter 
    if belongstoproblemsearch is not None:
        fields["cq:belongsToProblemSearch"] = belongstoproblemsearch 
    if belongstoproject is not None:
        fields["cq:belongsToProject"] = belongstoproject 
    if belongstoprojectfilter is not None:
        fields["cq:belongsToProjectFilter"] = belongstoprojectfilter 
    if belongstoprojectsearch is not None:
        fields["cq:belongsToProjectSearch"] = belongstoprojectsearch 
    if belongstorelease is not None:
        fields["cq:belongsToRelease"] = belongstorelease 
    if belongstoreleasefilter is not None:
        fields["cq:belongsToReleaseFilter"] = belongstoreleasefilter 
    if belongstoreleasesearch is not None:
        fields["cq:belongsToReleaseSearch"] = belongstoreleasesearch 
    if category is not None:
        fields["cq:Category"] = category 
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
    if dependsonworkitems is not None:
        fields["cq:dependsOnWorkitems"] = dependsonworkitems 
    if domain is not None:
        fields["cq:Domain"] = domain 
    if estimatedeffort is not None:
        fields["cq:EstimatedEffort"] = estimatedeffort 
    if estimationcomment is not None:
        fields["cq:EstimationComment"] = estimationcomment 
    if forms is not None:
        fields["cq:Forms"] = forms 
    if fulltextsearchtitle is not None:
        fields["cq:FullTextSearchTitle"] = fulltextsearchtitle 
    if hasbackground is not None:
        fields["cq:hasBackground"] = hasbackground 
    if hasdependentworkitems is not None:
        fields["cq:hasDependentWorkitems"] = hasdependentworkitems 
    if hasexternallinks is not None:
        fields["cq:hasExternalLinks"] = hasexternallinks 
    if hashistorylogs is not None:
        fields["cq:hasHistoryLogs"] = hashistorylogs 
    if haspredecessors is not None:
        fields["cq:hasPredecessors"] = haspredecessors 
    if hassuccessors is not None:
        fields["cq:hasSuccessors"] = hassuccessors 
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
    if planningmethod is not None:
        fields["cq:PlanningMethod"] = planningmethod 
    if priority is not None:
        fields["cq:Priority"] = priority 
    if product is not None:
        fields["cq:Product"] = product 
    if ratl_forminfo is not None:
        fields["cq:ratl_forminfo"] = ratl_forminfo 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
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
    if selectassignee is not None:
        fields["cq:SelectAssignee"] = selectassignee 
    if shorttitle is not None:
        fields["oslc:shortTitle"] = shorttitle 
    if startdate is not None:
        fields["cq:StartDate"] = startdate 
    if state is not None:
        fields["cq:State"] = state 
    if status is not None:
        fields["oslc_cm:status"] = status 
    if subcategory is not None:
        fields["cq:SubCategory"] = subcategory 
    if subject is not None:
        fields["dcterms:subject"] = subject 
    if submitdate is not None:
        fields["cq:SubmitDate"] = submitdate 
    if submitter is not None:
        fields["cq:Submitter"] = submitter 
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
    

    fields["dcterms:type"] = "Workitem"
    return Workitem(**fields)