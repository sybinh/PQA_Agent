
from .base import Resource, QueryStatement, BaseRecord
from pydantic import ConfigDict, Field, field_validator
import typing
from datetime import datetime, timezone
from enum import Enum
import json


from typing import TYPE_CHECKING

if TYPE_CHECKING:

    from .project import Project

    from .users import Users

    from .releasereleasemap import Releasereleasemap

    from .problem import Problem

    from .issuereleasemap import Issuereleasemap

    from .issue import Issue

    from .workitem import Workitem

    from .ratl_replicas import Ratl_Replicas

    from .release import Release



class Externallink(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    belongstoissue: typing.Optional[typing.Union[Resource, 'Issue']] = Field(alias="cq:belongsToIssue", default=None)
    
    belongstoissuereleasemap: typing.Optional[typing.Union[Resource, 'Issuereleasemap']] = Field(alias="cq:belongsToIssuereleasemap", default=None)
    
    belongstoproblem: typing.Optional[typing.Union[Resource, 'Problem']] = Field(alias="cq:belongsToProblem", default=None)
    
    belongstoproject: typing.Optional[typing.Union[Resource, 'Project']] = Field(alias="cq:belongsToProject", default=None)
    
    belongstorelease: typing.Optional[typing.Union[Resource, 'Release']] = Field(alias="cq:belongsToRelease", default=None)
    
    belongstoreleaserelmap: typing.Optional[typing.Union[Resource, 'Releasereleasemap']] = Field(alias="cq:belongsToReleaserelmap", default=None)
    
    belongstoworkitem: typing.Optional[typing.Union[Resource, 'Workitem']] = Field(alias="cq:belongsToWorkitem", default=None)
    
    creator: typing.Optional[Resource] = Field(alias="dcterms:creator", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    effectofthedefect: typing.Optional[str] = Field(alias="cq:EffectOfTheDefect", default=None)
    
    externaldescription: typing.Optional[str] = Field(alias="cq:ExternalDescription", default=None)
    
    extlinkcomment: typing.Optional[str] = Field(alias="cq:ExtLinkComment", default=None)
    
    extlinktype: typing.Optional[str] = Field(alias="cq:ExtLinkType", default=None)
    
    forms: typing.Optional[str] = Field(alias="cq:Forms", default=None)
    
    freetext: typing.Optional[str] = Field(alias="cq:Freetext", default=None)
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    historylog: typing.Optional[str] = Field(alias="cq:HistoryLog", default=None)
    
    id: typing.Optional[str] = Field(alias="cq:id", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    internaldescription: typing.Optional[str] = Field(alias="cq:InternalDescription", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    lastmodifieddate: typing.Optional[str] = Field(alias="cq:LastModifiedDate", default=None)
    
    lastmodifieduser: typing.Optional[str] = Field(alias="cq:LastModifiedUser", default=None)
    
    linkstate: typing.Optional[str] = Field(alias="cq:LinkState", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    lockcondition: typing.Optional[str] = Field(alias="cq:LockCondition", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    migration: typing.Optional[str] = Field(alias="cq:Migration", default=None)
    
    name: typing.Optional[str] = Field(alias="cq:Name", default=None)
    
    operationcontext: typing.Optional[str] = Field(alias="cq:OperationContext", default=None)
    
    operationmode: typing.Optional[str] = Field(alias="cq:OperationMode", default=None)
    
    ratl_forminfo: typing.Optional[str] = Field(alias="cq:ratl_forminfo", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    shorttitle: typing.Optional[str] = Field(alias="oslc:shortTitle", default=None)
    
    solution: typing.Optional[str] = Field(alias="cq:Solution", default=None)
    
    sourceid: typing.Optional[str] = Field(alias="cq:SourceID", default=None)
    
    sourcerecordtype: typing.Optional[str] = Field(alias="cq:SourceRecordType", default=None)
    
    submitdate: typing.Optional[str] = Field(alias="cq:SubmitDate", default=None)
    
    submitter: typing.Optional[str] = Field(alias="cq:Submitter", default=None)
    
    targetcompareid: typing.Optional[str] = Field(alias="cq:TargetCompareID", default=None)
    
    targetcomponentid: typing.Optional[str] = Field(alias="cq:TargetComponentID", default=None)
    
    targetconfigurationid: typing.Optional[str] = Field(alias="cq:TargetConfigurationID", default=None)
    
    targetconfigurationscope: typing.Optional[str] = Field(alias="cq:TargetConfigurationScope", default=None)
    
    targetconfigurationtype: typing.Optional[str] = Field(alias="cq:TargetConfigurationType", default=None)
    
    targetid: typing.Optional[str] = Field(alias="cq:TargetID", default=None)
    
    targetparameter: typing.Optional[str] = Field(alias="cq:TargetParameter", default=None)
    
    targetprojectid: typing.Optional[str] = Field(alias="cq:TargetProjectID", default=None)
    
    targetserver1: typing.Optional[str] = Field(alias="cq:TargetServer1", default=None)
    
    targetserver2: typing.Optional[str] = Field(alias="cq:TargetServer2", default=None)
    
    targetsystem: typing.Optional[str] = Field(alias="cq:TargetSystem", default=None)
    
    targettype: typing.Optional[str] = Field(alias="cq:TargetType", default=None)
    
    targeturl: typing.Optional[str] = Field(alias="cq:TargetURL", default=None)
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    validity: typing.Optional[str] = Field(alias="cq:Validity", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    
    versionname: typing.Optional[str] = Field(alias="cq:VersionName", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16784370"

    @classmethod
    def shape_name(cls) -> str:
        return "ExternalLink"

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
    


class ExternallinkProperty(Enum):
    belongstoissue = "cq:belongsToIssue"
    belongstoissuereleasemap = "cq:belongsToIssuereleasemap"
    belongstoproblem = "cq:belongsToProblem"
    belongstoproject = "cq:belongsToProject"
    belongstorelease = "cq:belongsToRelease"
    belongstoreleaserelmap = "cq:belongsToReleaserelmap"
    belongstoworkitem = "cq:belongsToWorkitem"
    creator = "dcterms:creator"
    dbid = "cq:dbid"
    dcterms__type = "dcterms:type"
    effectofthedefect = "cq:EffectOfTheDefect"
    externaldescription = "cq:ExternalDescription"
    extlinkcomment = "cq:ExtLinkComment"
    extlinktype = "cq:ExtLinkType"
    forms = "cq:Forms"
    freetext = "cq:Freetext"
    history = "cq:history"
    historylog = "cq:HistoryLog"
    id = "cq:id"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    internaldescription = "cq:InternalDescription"
    is_active = "cq:is_active"
    lastmodifieddate = "cq:LastModifiedDate"
    lastmodifieduser = "cq:LastModifiedUser"
    linkstate = "cq:LinkState"
    lock_version = "cq:lock_version"
    lockcondition = "cq:LockCondition"
    locked_by = "cq:locked_by"
    migration = "cq:Migration"
    name = "cq:Name"
    operationcontext = "cq:OperationContext"
    operationmode = "cq:OperationMode"
    ratl_forminfo = "cq:ratl_forminfo"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    shorttitle = "oslc:shortTitle"
    solution = "cq:Solution"
    sourceid = "cq:SourceID"
    sourcerecordtype = "cq:SourceRecordType"
    submitdate = "cq:SubmitDate"
    submitter = "cq:Submitter"
    targetcompareid = "cq:TargetCompareID"
    targetcomponentid = "cq:TargetComponentID"
    targetconfigurationid = "cq:TargetConfigurationID"
    targetconfigurationscope = "cq:TargetConfigurationScope"
    targetconfigurationtype = "cq:TargetConfigurationType"
    targetid = "cq:TargetID"
    targetparameter = "cq:TargetParameter"
    targetprojectid = "cq:TargetProjectID"
    targetserver1 = "cq:TargetServer1"
    targetserver2 = "cq:TargetServer2"
    targetsystem = "cq:TargetSystem"
    targettype = "cq:TargetType"
    targeturl = "cq:TargetURL"
    title = "dcterms:title"
    validity = "cq:Validity"
    version = "cq:version"
    versionname = "cq:VersionName"
    

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


def create_externallink_changeset(
    belongstoissue: typing.Optional[typing.Union[Resource, 'Issue']] = None,
    belongstoissuereleasemap: typing.Optional[typing.Union[Resource, 'Issuereleasemap']] = None,
    belongstoproblem: typing.Optional[typing.Union[Resource, 'Problem']] = None,
    belongstoproject: typing.Optional[typing.Union[Resource, 'Project']] = None,
    belongstorelease: typing.Optional[typing.Union[Resource, 'Release']] = None,
    belongstoreleaserelmap: typing.Optional[typing.Union[Resource, 'Releasereleasemap']] = None,
    belongstoworkitem: typing.Optional[typing.Union[Resource, 'Workitem']] = None,
    creator: typing.Optional[Resource] = None,
    dbid: typing.Optional[str] = None,
    effectofthedefect: typing.Optional[str] = None,
    externaldescription: typing.Optional[str] = None,
    extlinkcomment: typing.Optional[str] = None,
    extlinktype: typing.Optional[str] = None,
    forms: typing.Optional[str] = None,
    freetext: typing.Optional[str] = None,
    history: typing.Optional[Resource] = None,
    historylog: typing.Optional[str] = None,
    id: typing.Optional[str] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    internaldescription: typing.Optional[str] = None,
    is_active: typing.Optional[int] = None,
    lastmodifieddate: typing.Optional[str] = None,
    lastmodifieduser: typing.Optional[str] = None,
    linkstate: typing.Optional[str] = None,
    lock_version: typing.Optional[int] = None,
    lockcondition: typing.Optional[str] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    migration: typing.Optional[str] = None,
    name: typing.Optional[str] = None,
    operationcontext: typing.Optional[str] = None,
    operationmode: typing.Optional[str] = None,
    ratl_forminfo: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    shorttitle: typing.Optional[str] = None,
    solution: typing.Optional[str] = None,
    sourceid: typing.Optional[str] = None,
    sourcerecordtype: typing.Optional[str] = None,
    submitdate: typing.Optional[str] = None,
    submitter: typing.Optional[str] = None,
    targetcompareid: typing.Optional[str] = None,
    targetcomponentid: typing.Optional[str] = None,
    targetconfigurationid: typing.Optional[str] = None,
    targetconfigurationscope: typing.Optional[str] = None,
    targetconfigurationtype: typing.Optional[str] = None,
    targetid: typing.Optional[str] = None,
    targetparameter: typing.Optional[str] = None,
    targetprojectid: typing.Optional[str] = None,
    targetserver1: typing.Optional[str] = None,
    targetserver2: typing.Optional[str] = None,
    targetsystem: typing.Optional[str] = None,
    targettype: typing.Optional[str] = None,
    targeturl: typing.Optional[str] = None,
    title: typing.Optional[str] = None,
    validity: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    versionname: typing.Optional[str] = None,
    
) -> Externallink:
    '''
    Create a Externallink changeset.
    '''
    fields = {}
    if belongstoissue is not None:
        fields["cq:belongsToIssue"] = belongstoissue 
    if belongstoissuereleasemap is not None:
        fields["cq:belongsToIssuereleasemap"] = belongstoissuereleasemap 
    if belongstoproblem is not None:
        fields["cq:belongsToProblem"] = belongstoproblem 
    if belongstoproject is not None:
        fields["cq:belongsToProject"] = belongstoproject 
    if belongstorelease is not None:
        fields["cq:belongsToRelease"] = belongstorelease 
    if belongstoreleaserelmap is not None:
        fields["cq:belongsToReleaserelmap"] = belongstoreleaserelmap 
    if belongstoworkitem is not None:
        fields["cq:belongsToWorkitem"] = belongstoworkitem 
    if creator is not None:
        fields["dcterms:creator"] = creator 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if effectofthedefect is not None:
        fields["cq:EffectOfTheDefect"] = effectofthedefect 
    if externaldescription is not None:
        fields["cq:ExternalDescription"] = externaldescription 
    if extlinkcomment is not None:
        fields["cq:ExtLinkComment"] = extlinkcomment 
    if extlinktype is not None:
        fields["cq:ExtLinkType"] = extlinktype 
    if forms is not None:
        fields["cq:Forms"] = forms 
    if freetext is not None:
        fields["cq:Freetext"] = freetext 
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
    if internaldescription is not None:
        fields["cq:InternalDescription"] = internaldescription 
    if is_active is not None:
        fields["cq:is_active"] = is_active 
    if lastmodifieddate is not None:
        fields["cq:LastModifiedDate"] = lastmodifieddate 
    if lastmodifieduser is not None:
        fields["cq:LastModifiedUser"] = lastmodifieduser 
    if linkstate is not None:
        fields["cq:LinkState"] = linkstate 
    if lock_version is not None:
        fields["cq:lock_version"] = lock_version 
    if lockcondition is not None:
        fields["cq:LockCondition"] = lockcondition 
    if locked_by is not None:
        fields["cq:locked_by"] = locked_by 
    if migration is not None:
        fields["cq:Migration"] = migration 
    if name is not None:
        fields["cq:Name"] = name 
    if operationcontext is not None:
        fields["cq:OperationContext"] = operationcontext 
    if operationmode is not None:
        fields["cq:OperationMode"] = operationmode 
    if ratl_forminfo is not None:
        fields["cq:ratl_forminfo"] = ratl_forminfo 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if shorttitle is not None:
        fields["oslc:shortTitle"] = shorttitle 
    if solution is not None:
        fields["cq:Solution"] = solution 
    if sourceid is not None:
        fields["cq:SourceID"] = sourceid 
    if sourcerecordtype is not None:
        fields["cq:SourceRecordType"] = sourcerecordtype 
    if submitdate is not None:
        fields["cq:SubmitDate"] = submitdate 
    if submitter is not None:
        fields["cq:Submitter"] = submitter 
    if targetcompareid is not None:
        fields["cq:TargetCompareID"] = targetcompareid 
    if targetcomponentid is not None:
        fields["cq:TargetComponentID"] = targetcomponentid 
    if targetconfigurationid is not None:
        fields["cq:TargetConfigurationID"] = targetconfigurationid 
    if targetconfigurationscope is not None:
        fields["cq:TargetConfigurationScope"] = targetconfigurationscope 
    if targetconfigurationtype is not None:
        fields["cq:TargetConfigurationType"] = targetconfigurationtype 
    if targetid is not None:
        fields["cq:TargetID"] = targetid 
    if targetparameter is not None:
        fields["cq:TargetParameter"] = targetparameter 
    if targetprojectid is not None:
        fields["cq:TargetProjectID"] = targetprojectid 
    if targetserver1 is not None:
        fields["cq:TargetServer1"] = targetserver1 
    if targetserver2 is not None:
        fields["cq:TargetServer2"] = targetserver2 
    if targetsystem is not None:
        fields["cq:TargetSystem"] = targetsystem 
    if targettype is not None:
        fields["cq:TargetType"] = targettype 
    if targeturl is not None:
        fields["cq:TargetURL"] = targeturl 
    if title is not None:
        fields["dcterms:title"] = title 
    if validity is not None:
        fields["cq:Validity"] = validity 
    if version is not None:
        fields["cq:version"] = version 
    if versionname is not None:
        fields["cq:VersionName"] = versionname 
    

    fields["dcterms:type"] = "ExternalLink"
    return Externallink(**fields)