
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

    from .issue import Issue

    from .workitem import Workitem

    from .background import Background

    from .ratl_replicas import Ratl_Replicas



class Problem(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    accountnumbers: typing.Optional[str] = Field(alias="cq:AccountNumbers", default=None)
    
    affectedecugenerations: typing.Optional[str] = Field(alias="cq:AffectedECUGenerations", default=None)
    
    alertnotification: typing.Optional[str] = Field(alias="cq:AlertNotification", default=None)
    
    assignee: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:Assignee", default=None)
    
    assigneefilter: typing.Optional[str] = Field(alias="cq:AssigneeFilter", default=None)
    
    assigneesearch: typing.Optional[str] = Field(alias="cq:AssigneeSearch", default=None)
    
    attachments: typing.Optional[Resource] = Field(alias="cq:Attachments", default=None)
    
    belongstoproject: typing.Optional[typing.Union[Resource, 'Project']] = Field(alias="cq:belongsToProject", default=None)
    
    belongstoprojectfilter: typing.Optional[str] = Field(alias="cq:belongsToProjectFilter", default=None)
    
    belongstoprojectsearch: typing.Optional[str] = Field(alias="cq:belongsToProjectSearch", default=None)
    
    calibrationdata: typing.Optional[str] = Field(alias="cq:CalibrationData", default=None)
    
    contactinformation: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:ContactInformation", default=None)
    
    contactinformationfilter: typing.Optional[str] = Field(alias="cq:ContactInformationFilter", default=None)
    
    contactinformationsearch: typing.Optional[str] = Field(alias="cq:ContactInformationSearch", default=None)
    
    cq__Description: typing.Optional[str] = Field(alias="cq:Description", default=None)
    
    cq__Title: typing.Optional[str] = Field(alias="cq:Title", default=None)
    
    cq__Type: typing.Optional[str] = Field(alias="cq:Type", default=None)
    
    creationmode: typing.Optional[str] = Field(alias="cq:CreationMode", default=None)
    
    creator: typing.Optional[Resource] = Field(alias="dcterms:creator", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__description: typing.Optional[str] = Field(alias="dcterms:description", default=None)
    
    dcterms__title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    dependencies: typing.Optional[str] = Field(alias="cq:Dependencies", default=None)
    
    developmentenvironment: typing.Optional[str] = Field(alias="cq:DevelopmentEnvironment", default=None)
    
    domain: typing.Optional[str] = Field(alias="cq:Domain", default=None)
    
    duedate: typing.Optional[datetime] = Field(alias="cq:DueDate", default=None)
    
    duplicateof: typing.Optional[typing.Union[Resource, 'Problem']] = Field(alias="cq:duplicateOf", default=None)
    
    duplicateoffilter: typing.Optional[str] = Field(alias="cq:duplicateOfFilter", default=None)
    
    duplicateofsearch: typing.Optional[str] = Field(alias="cq:duplicateOfSearch", default=None)
    
    ecuhardware: typing.Optional[str] = Field(alias="cq:ECUHardware", default=None)
    
    effectofthedefect: typing.Optional[str] = Field(alias="cq:EffectOfTheDefect", default=None)
    
    errordescription: typing.Optional[str] = Field(alias="cq:ErrorDescription", default=None)
    
    externaldescription: typing.Optional[str] = Field(alias="cq:ExternalDescription", default=None)
    
    externalecucalibration: typing.Optional[str] = Field(alias="cq:ExternalECUCalibration", default=None)
    
    externalecusoftware: typing.Optional[str] = Field(alias="cq:ExternalECUSoftware", default=None)
    
    externalhistory: typing.Optional[str] = Field(alias="cq:ExternalHistory", default=None)
    
    externalid: typing.Optional[str] = Field(alias="cq:ExternalID", default=None)
    
    externalinformation: typing.Optional[str] = Field(alias="cq:ExternalInformation", default=None)
    
    finalresolution: typing.Optional[str] = Field(alias="cq:FinalResolution", default=None)
    
    forms: typing.Optional[str] = Field(alias="cq:Forms", default=None)
    
    froms: typing.Optional[str] = Field(alias="cq:Froms", default=None)
    
    fulltextsearchtitle: typing.Optional[str] = Field(alias="cq:FullTextSearchTitle", default=None)
    
    hardware: typing.Optional[str] = Field(alias="cq:Hardware", default=None)
    
    hasbackground: typing.Optional[typing.Union[Resource, 'Background']] = Field(alias="cq:hasBackground", default=None)
    
    hasduplicates: typing.List[typing.Union[Resource, 'Problem']] = Field(alias="cq:hasDuplicates", default_factory=lambda: [])
    
    hasexternallinks: typing.List[typing.Union[Resource, 'Externallink']] = Field(alias="cq:hasExternalLinks", default_factory=lambda: [])
    
    hashistorylogs: typing.List[typing.Union[Resource, 'Historylog']] = Field(alias="cq:hasHistoryLogs", default_factory=lambda: [])
    
    hasissues: typing.List[typing.Union[Resource, 'Issue']] = Field(alias="cq:hasIssues", default_factory=lambda: [])
    
    hasworkitems: typing.List[typing.Union[Resource, 'Workitem']] = Field(alias="cq:hasWorkitems", default_factory=lambda: [])
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    id: typing.Optional[str] = Field(alias="cq:id", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    idqcentralorganization: typing.Optional[str] = Field(alias="cq:IDQCentralOrganization", default=None)
    
    impactanalysisresult: typing.Optional[str] = Field(alias="cq:ImpactAnalysisResult", default=None)
    
    insertstatement: typing.Optional[str] = Field(alias="cq:InsertStatement", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    internalcomment: typing.Optional[str] = Field(alias="cq:InternalComment", default=None)
    
    internalecucalibration: typing.Optional[str] = Field(alias="cq:InternalECUCalibration", default=None)
    
    internalecusoftware: typing.Optional[str] = Field(alias="cq:InternalECUSoftware", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    is_duplicate: typing.Optional[int] = Field(alias="cq:is_duplicate", default=None)
    
    issuedby: typing.Optional[str] = Field(alias="cq:IssuedBy", default=None)
    
    issuerclass: typing.Optional[str] = Field(alias="cq:IssuerClass", default=None)
    
    justification: typing.Optional[str] = Field(alias="cq:Justification", default=None)
    
    lifecyclestate: typing.Optional[str] = Field(alias="cq:LifeCycleState", default=None)
    
    lifecyclestatecomment: typing.Optional[str] = Field(alias="cq:LifeCycleStateComment", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    operationcontext: typing.Optional[str] = Field(alias="cq:OperationContext", default=None)
    
    operationmode: typing.Optional[str] = Field(alias="cq:OperationMode", default=None)
    
    othercomponents: typing.Optional[str] = Field(alias="cq:OtherComponents", default=None)
    
    primaryrootcause: typing.Optional[str] = Field(alias="cq:PrimaryRootCause", default=None)
    
    priority: typing.Optional[str] = Field(alias="cq:Priority", default=None)
    
    problemreproduction: typing.Optional[str] = Field(alias="cq:ProblemReproduction", default=None)
    
    product: typing.Optional[str] = Field(alias="cq:Product", default=None)
    
    ratl_forminfo: typing.Optional[str] = Field(alias="cq:ratl_forminfo", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    recommendations: typing.Optional[str] = Field(alias="cq:Recommendations", default=None)
    
    reportid8d: typing.Optional[str] = Field(alias="cq:ReportID8D", default=None)
    
    rootcauseanalysisresult: typing.Optional[str] = Field(alias="cq:RootCauseAnalysisResult", default=None)
    
    rotitle: typing.Optional[str] = Field(alias="cq:ROTitle", default=None)
    
    rotype: typing.Optional[str] = Field(alias="cq:ROType", default=None)
    
    severity: typing.Optional[str] = Field(alias="cq:Severity", default=None)
    
    shorttitle: typing.Optional[str] = Field(alias="oslc:shortTitle", default=None)
    
    state: typing.Optional[str] = Field(alias="cq:State", default=None)
    
    status: typing.Optional[str] = Field(alias="oslc_cm:status", default=None)
    
    subject: typing.List[str] = Field(alias="dcterms:subject", default_factory=lambda: [])
    
    submitdate: typing.Optional[datetime] = Field(alias="cq:SubmitDate", default=None)
    
    submitter: typing.Optional[str] = Field(alias="cq:Submitter", default=None)
    
    suppressvalidation: typing.Optional[str] = Field(alias="cq:SuppressValidation", default=None)
    
    suppressvalidationcomment: typing.Optional[str] = Field(alias="cq:SuppressValidationComment", default=None)
    
    systemconstants: typing.Optional[str] = Field(alias="cq:SystemConstants", default=None)
    
    tags: typing.Optional[str] = Field(alias="cq:Tags", default=None)
    
    template: typing.Optional[str] = Field(alias="cq:Template", default=None)
    
    testingenvironment: typing.Optional[str] = Field(alias="cq:TestingEnvironment", default=None)
    
    unduplicate_state: typing.Optional[str] = Field(alias="cq:unduplicate_state", default=None)
    
    urgentalertnotification: typing.Optional[str] = Field(alias="cq:UrgentAlertNotification", default=None)
    
    urgentresolution: typing.Optional[str] = Field(alias="cq:UrgentResolution", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    
    versionhistory: typing.Optional[str] = Field(alias="cq:VersionHistory", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16784848"

    @classmethod
    def shape_name(cls) -> str:
        return "Problem"

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
    


class ProblemProperty(Enum):
    accountnumbers = "cq:AccountNumbers"
    affectedecugenerations = "cq:AffectedECUGenerations"
    alertnotification = "cq:AlertNotification"
    assignee = "cq:Assignee"
    assigneefilter = "cq:AssigneeFilter"
    assigneesearch = "cq:AssigneeSearch"
    attachments = "cq:Attachments"
    belongstoproject = "cq:belongsToProject"
    belongstoprojectfilter = "cq:belongsToProjectFilter"
    belongstoprojectsearch = "cq:belongsToProjectSearch"
    calibrationdata = "cq:CalibrationData"
    contactinformation = "cq:ContactInformation"
    contactinformationfilter = "cq:ContactInformationFilter"
    contactinformationsearch = "cq:ContactInformationSearch"
    cq__Description = "cq:Description"
    cq__Title = "cq:Title"
    cq__Type = "cq:Type"
    creationmode = "cq:CreationMode"
    creator = "dcterms:creator"
    dbid = "cq:dbid"
    dcterms__description = "dcterms:description"
    dcterms__title = "dcterms:title"
    dcterms__type = "dcterms:type"
    dependencies = "cq:Dependencies"
    developmentenvironment = "cq:DevelopmentEnvironment"
    domain = "cq:Domain"
    duedate = "cq:DueDate"
    duplicateof = "cq:duplicateOf"
    duplicateoffilter = "cq:duplicateOfFilter"
    duplicateofsearch = "cq:duplicateOfSearch"
    ecuhardware = "cq:ECUHardware"
    effectofthedefect = "cq:EffectOfTheDefect"
    errordescription = "cq:ErrorDescription"
    externaldescription = "cq:ExternalDescription"
    externalecucalibration = "cq:ExternalECUCalibration"
    externalecusoftware = "cq:ExternalECUSoftware"
    externalhistory = "cq:ExternalHistory"
    externalid = "cq:ExternalID"
    externalinformation = "cq:ExternalInformation"
    finalresolution = "cq:FinalResolution"
    forms = "cq:Forms"
    froms = "cq:Froms"
    fulltextsearchtitle = "cq:FullTextSearchTitle"
    hardware = "cq:Hardware"
    hasbackground = "cq:hasBackground"
    hasduplicates = "cq:hasDuplicates"
    hasexternallinks = "cq:hasExternalLinks"
    hashistorylogs = "cq:hasHistoryLogs"
    hasissues = "cq:hasIssues"
    hasworkitems = "cq:hasWorkitems"
    history = "cq:history"
    id = "cq:id"
    identifier = "dcterms:identifier"
    idqcentralorganization = "cq:IDQCentralOrganization"
    impactanalysisresult = "cq:ImpactAnalysisResult"
    insertstatement = "cq:InsertStatement"
    instanceshape = "oslc:instanceShape"
    internalcomment = "cq:InternalComment"
    internalecucalibration = "cq:InternalECUCalibration"
    internalecusoftware = "cq:InternalECUSoftware"
    is_active = "cq:is_active"
    is_duplicate = "cq:is_duplicate"
    issuedby = "cq:IssuedBy"
    issuerclass = "cq:IssuerClass"
    justification = "cq:Justification"
    lifecyclestate = "cq:LifeCycleState"
    lifecyclestatecomment = "cq:LifeCycleStateComment"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    operationcontext = "cq:OperationContext"
    operationmode = "cq:OperationMode"
    othercomponents = "cq:OtherComponents"
    primaryrootcause = "cq:PrimaryRootCause"
    priority = "cq:Priority"
    problemreproduction = "cq:ProblemReproduction"
    product = "cq:Product"
    ratl_forminfo = "cq:ratl_forminfo"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    recommendations = "cq:Recommendations"
    reportid8d = "cq:ReportID8D"
    rootcauseanalysisresult = "cq:RootCauseAnalysisResult"
    rotitle = "cq:ROTitle"
    rotype = "cq:ROType"
    severity = "cq:Severity"
    shorttitle = "oslc:shortTitle"
    state = "cq:State"
    status = "oslc_cm:status"
    subject = "dcterms:subject"
    submitdate = "cq:SubmitDate"
    submitter = "cq:Submitter"
    suppressvalidation = "cq:SuppressValidation"
    suppressvalidationcomment = "cq:SuppressValidationComment"
    systemconstants = "cq:SystemConstants"
    tags = "cq:Tags"
    template = "cq:Template"
    testingenvironment = "cq:TestingEnvironment"
    unduplicate_state = "cq:unduplicate_state"
    urgentalertnotification = "cq:UrgentAlertNotification"
    urgentresolution = "cq:UrgentResolution"
    version = "cq:version"
    versionhistory = "cq:VersionHistory"
    

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


def create_problem_changeset(
    accountnumbers: typing.Optional[str] = None,
    affectedecugenerations: typing.Optional[str] = None,
    alertnotification: typing.Optional[str] = None,
    assignee: typing.Optional[typing.Union[Resource, 'Users']] = None,
    assigneefilter: typing.Optional[str] = None,
    assigneesearch: typing.Optional[str] = None,
    attachments: typing.Optional[Resource] = None,
    belongstoproject: typing.Optional[typing.Union[Resource, 'Project']] = None,
    belongstoprojectfilter: typing.Optional[str] = None,
    belongstoprojectsearch: typing.Optional[str] = None,
    calibrationdata: typing.Optional[str] = None,
    contactinformation: typing.Optional[typing.Union[Resource, 'Users']] = None,
    contactinformationfilter: typing.Optional[str] = None,
    contactinformationsearch: typing.Optional[str] = None,
    cq__Description: typing.Optional[str] = None,
    cq__Title: typing.Optional[str] = None,
    cq__Type: typing.Optional[str] = None,
    creationmode: typing.Optional[str] = None,
    creator: typing.Optional[Resource] = None,
    dbid: typing.Optional[str] = None,
    dcterms__description: typing.Optional[str] = None,
    dcterms__title: typing.Optional[str] = None,
    dependencies: typing.Optional[str] = None,
    developmentenvironment: typing.Optional[str] = None,
    domain: typing.Optional[str] = None,
    duedate: typing.Optional[datetime] = None,
    duplicateof: typing.Optional[typing.Union[Resource, 'Problem']] = None,
    duplicateoffilter: typing.Optional[str] = None,
    duplicateofsearch: typing.Optional[str] = None,
    ecuhardware: typing.Optional[str] = None,
    effectofthedefect: typing.Optional[str] = None,
    errordescription: typing.Optional[str] = None,
    externaldescription: typing.Optional[str] = None,
    externalecucalibration: typing.Optional[str] = None,
    externalecusoftware: typing.Optional[str] = None,
    externalhistory: typing.Optional[str] = None,
    externalid: typing.Optional[str] = None,
    externalinformation: typing.Optional[str] = None,
    finalresolution: typing.Optional[str] = None,
    forms: typing.Optional[str] = None,
    froms: typing.Optional[str] = None,
    fulltextsearchtitle: typing.Optional[str] = None,
    hardware: typing.Optional[str] = None,
    hasbackground: typing.Optional[typing.Union[Resource, 'Background']] = None,
    hasduplicates: typing.Optional[typing.List[typing.Union[Resource, 'Problem']]] = None,
    hasexternallinks: typing.Optional[typing.List[typing.Union[Resource, 'Externallink']]] = None,
    hashistorylogs: typing.Optional[typing.List[typing.Union[Resource, 'Historylog']]] = None,
    hasissues: typing.Optional[typing.List[typing.Union[Resource, 'Issue']]] = None,
    hasworkitems: typing.Optional[typing.List[typing.Union[Resource, 'Workitem']]] = None,
    history: typing.Optional[Resource] = None,
    id: typing.Optional[str] = None,
    identifier: typing.Optional[str] = None,
    idqcentralorganization: typing.Optional[str] = None,
    impactanalysisresult: typing.Optional[str] = None,
    insertstatement: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    internalcomment: typing.Optional[str] = None,
    internalecucalibration: typing.Optional[str] = None,
    internalecusoftware: typing.Optional[str] = None,
    is_active: typing.Optional[int] = None,
    is_duplicate: typing.Optional[int] = None,
    issuedby: typing.Optional[str] = None,
    issuerclass: typing.Optional[str] = None,
    justification: typing.Optional[str] = None,
    lifecyclestate: typing.Optional[str] = None,
    lifecyclestatecomment: typing.Optional[str] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    operationcontext: typing.Optional[str] = None,
    operationmode: typing.Optional[str] = None,
    othercomponents: typing.Optional[str] = None,
    primaryrootcause: typing.Optional[str] = None,
    priority: typing.Optional[str] = None,
    problemreproduction: typing.Optional[str] = None,
    product: typing.Optional[str] = None,
    ratl_forminfo: typing.Optional[str] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    recommendations: typing.Optional[str] = None,
    reportid8d: typing.Optional[str] = None,
    rootcauseanalysisresult: typing.Optional[str] = None,
    rotitle: typing.Optional[str] = None,
    rotype: typing.Optional[str] = None,
    severity: typing.Optional[str] = None,
    shorttitle: typing.Optional[str] = None,
    state: typing.Optional[str] = None,
    status: typing.Optional[str] = None,
    subject: typing.Optional[typing.List[str]] = None,
    submitdate: typing.Optional[datetime] = None,
    submitter: typing.Optional[str] = None,
    suppressvalidation: typing.Optional[str] = None,
    suppressvalidationcomment: typing.Optional[str] = None,
    systemconstants: typing.Optional[str] = None,
    tags: typing.Optional[str] = None,
    template: typing.Optional[str] = None,
    testingenvironment: typing.Optional[str] = None,
    unduplicate_state: typing.Optional[str] = None,
    urgentalertnotification: typing.Optional[str] = None,
    urgentresolution: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    versionhistory: typing.Optional[str] = None,
    
) -> Problem:
    '''
    Create a Problem changeset.
    '''
    fields = {}
    if accountnumbers is not None:
        fields["cq:AccountNumbers"] = accountnumbers 
    if affectedecugenerations is not None:
        fields["cq:AffectedECUGenerations"] = affectedecugenerations 
    if alertnotification is not None:
        fields["cq:AlertNotification"] = alertnotification 
    if assignee is not None:
        fields["cq:Assignee"] = assignee 
    if assigneefilter is not None:
        fields["cq:AssigneeFilter"] = assigneefilter 
    if assigneesearch is not None:
        fields["cq:AssigneeSearch"] = assigneesearch 
    if attachments is not None:
        fields["cq:Attachments"] = attachments 
    if belongstoproject is not None:
        fields["cq:belongsToProject"] = belongstoproject 
    if belongstoprojectfilter is not None:
        fields["cq:belongsToProjectFilter"] = belongstoprojectfilter 
    if belongstoprojectsearch is not None:
        fields["cq:belongsToProjectSearch"] = belongstoprojectsearch 
    if calibrationdata is not None:
        fields["cq:CalibrationData"] = calibrationdata 
    if contactinformation is not None:
        fields["cq:ContactInformation"] = contactinformation 
    if contactinformationfilter is not None:
        fields["cq:ContactInformationFilter"] = contactinformationfilter 
    if contactinformationsearch is not None:
        fields["cq:ContactInformationSearch"] = contactinformationsearch 
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
    if dependencies is not None:
        fields["cq:Dependencies"] = dependencies 
    if developmentenvironment is not None:
        fields["cq:DevelopmentEnvironment"] = developmentenvironment 
    if domain is not None:
        fields["cq:Domain"] = domain 
    if duedate is not None:
        fields["cq:DueDate"] = duedate 
    if duplicateof is not None:
        fields["cq:duplicateOf"] = duplicateof 
    if duplicateoffilter is not None:
        fields["cq:duplicateOfFilter"] = duplicateoffilter 
    if duplicateofsearch is not None:
        fields["cq:duplicateOfSearch"] = duplicateofsearch 
    if ecuhardware is not None:
        fields["cq:ECUHardware"] = ecuhardware 
    if effectofthedefect is not None:
        fields["cq:EffectOfTheDefect"] = effectofthedefect 
    if errordescription is not None:
        fields["cq:ErrorDescription"] = errordescription 
    if externaldescription is not None:
        fields["cq:ExternalDescription"] = externaldescription 
    if externalecucalibration is not None:
        fields["cq:ExternalECUCalibration"] = externalecucalibration 
    if externalecusoftware is not None:
        fields["cq:ExternalECUSoftware"] = externalecusoftware 
    if externalhistory is not None:
        fields["cq:ExternalHistory"] = externalhistory 
    if externalid is not None:
        fields["cq:ExternalID"] = externalid 
    if externalinformation is not None:
        fields["cq:ExternalInformation"] = externalinformation 
    if finalresolution is not None:
        fields["cq:FinalResolution"] = finalresolution 
    if forms is not None:
        fields["cq:Forms"] = forms 
    if froms is not None:
        fields["cq:Froms"] = froms 
    if fulltextsearchtitle is not None:
        fields["cq:FullTextSearchTitle"] = fulltextsearchtitle 
    if hardware is not None:
        fields["cq:Hardware"] = hardware 
    if hasbackground is not None:
        fields["cq:hasBackground"] = hasbackground 
    if hasduplicates is not None:
        fields["cq:hasDuplicates"] = hasduplicates 
    if hasexternallinks is not None:
        fields["cq:hasExternalLinks"] = hasexternallinks 
    if hashistorylogs is not None:
        fields["cq:hasHistoryLogs"] = hashistorylogs 
    if hasissues is not None:
        fields["cq:hasIssues"] = hasissues 
    if hasworkitems is not None:
        fields["cq:hasWorkitems"] = hasworkitems 
    if history is not None:
        fields["cq:history"] = history 
    if id is not None:
        fields["cq:id"] = id 
    if identifier is not None:
        fields["dcterms:identifier"] = identifier 
    if idqcentralorganization is not None:
        fields["cq:IDQCentralOrganization"] = idqcentralorganization 
    if impactanalysisresult is not None:
        fields["cq:ImpactAnalysisResult"] = impactanalysisresult 
    if insertstatement is not None:
        fields["cq:InsertStatement"] = insertstatement 
    if instanceshape is not None:
        fields["oslc:instanceShape"] = instanceshape 
    if internalcomment is not None:
        fields["cq:InternalComment"] = internalcomment 
    if internalecucalibration is not None:
        fields["cq:InternalECUCalibration"] = internalecucalibration 
    if internalecusoftware is not None:
        fields["cq:InternalECUSoftware"] = internalecusoftware 
    if is_active is not None:
        fields["cq:is_active"] = is_active 
    if is_duplicate is not None:
        fields["cq:is_duplicate"] = is_duplicate 
    if issuedby is not None:
        fields["cq:IssuedBy"] = issuedby 
    if issuerclass is not None:
        fields["cq:IssuerClass"] = issuerclass 
    if justification is not None:
        fields["cq:Justification"] = justification 
    if lifecyclestate is not None:
        fields["cq:LifeCycleState"] = lifecyclestate 
    if lifecyclestatecomment is not None:
        fields["cq:LifeCycleStateComment"] = lifecyclestatecomment 
    if lock_version is not None:
        fields["cq:lock_version"] = lock_version 
    if locked_by is not None:
        fields["cq:locked_by"] = locked_by 
    if operationcontext is not None:
        fields["cq:OperationContext"] = operationcontext 
    if operationmode is not None:
        fields["cq:OperationMode"] = operationmode 
    if othercomponents is not None:
        fields["cq:OtherComponents"] = othercomponents 
    if primaryrootcause is not None:
        fields["cq:PrimaryRootCause"] = primaryrootcause 
    if priority is not None:
        fields["cq:Priority"] = priority 
    if problemreproduction is not None:
        fields["cq:ProblemReproduction"] = problemreproduction 
    if product is not None:
        fields["cq:Product"] = product 
    if ratl_forminfo is not None:
        fields["cq:ratl_forminfo"] = ratl_forminfo 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if recommendations is not None:
        fields["cq:Recommendations"] = recommendations 
    if reportid8d is not None:
        fields["cq:ReportID8D"] = reportid8d 
    if rootcauseanalysisresult is not None:
        fields["cq:RootCauseAnalysisResult"] = rootcauseanalysisresult 
    if rotitle is not None:
        fields["cq:ROTitle"] = rotitle 
    if rotype is not None:
        fields["cq:ROType"] = rotype 
    if severity is not None:
        fields["cq:Severity"] = severity 
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
    if suppressvalidation is not None:
        fields["cq:SuppressValidation"] = suppressvalidation 
    if suppressvalidationcomment is not None:
        fields["cq:SuppressValidationComment"] = suppressvalidationcomment 
    if systemconstants is not None:
        fields["cq:SystemConstants"] = systemconstants 
    if tags is not None:
        fields["cq:Tags"] = tags 
    if template is not None:
        fields["cq:Template"] = template 
    if testingenvironment is not None:
        fields["cq:TestingEnvironment"] = testingenvironment 
    if unduplicate_state is not None:
        fields["cq:unduplicate_state"] = unduplicate_state 
    if urgentalertnotification is not None:
        fields["cq:UrgentAlertNotification"] = urgentalertnotification 
    if urgentresolution is not None:
        fields["cq:UrgentResolution"] = urgentresolution 
    if version is not None:
        fields["cq:version"] = version 
    if versionhistory is not None:
        fields["cq:VersionHistory"] = versionhistory 
    

    fields["dcterms:type"] = "Problem"
    return Problem(**fields)