
from .base import Resource, QueryStatement, BaseRecord
from pydantic import ConfigDict, Field, field_validator
import typing
from datetime import datetime, timezone
from enum import Enum
import json


from typing import TYPE_CHECKING

if TYPE_CHECKING:

    from .commercial import Commercial

    from .issueissuemap import Issueissuemap

    from .project import Project

    from .users import Users

    from .exchangeprotocol import Exchangeprotocol

    from .releasereleasemap import Releasereleasemap

    from .problem import Problem

    from .issuereleasemap import Issuereleasemap

    from .issue import Issue

    from .workitem import Workitem

    from .ratl_replicas import Ratl_Replicas

    from .release import Release

    from .contact import Contact



class Historylog(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    actionname: typing.Optional[str] = Field(alias="cq:ActionName", default=None)
    
    belongstocommercial: typing.Optional[typing.Union[Resource, 'Commercial']] = Field(alias="cq:belongsToCommercial", default=None)
    
    belongstocontact: typing.Optional[typing.Union[Resource, 'Contact']] = Field(alias="cq:belongsToContact", default=None)
    
    belongstoexchangeprotocol: typing.Optional[typing.Union[Resource, 'Exchangeprotocol']] = Field(alias="cq:belongsToExchangeProtocol", default=None)
    
    belongstoissue: typing.Optional[typing.Union[Resource, 'Issue']] = Field(alias="cq:belongsToIssue", default=None)
    
    belongstoissueissuemap: typing.Optional[typing.Union[Resource, 'Issueissuemap']] = Field(alias="cq:belongsToIssueissuemap", default=None)
    
    belongstoissuereleasemap: typing.Optional[typing.Union[Resource, 'Issuereleasemap']] = Field(alias="cq:belongsToIssuereleasemap", default=None)
    
    belongstoproblem: typing.Optional[typing.Union[Resource, 'Problem']] = Field(alias="cq:belongsToProblem", default=None)
    
    belongstoproject: typing.Optional[typing.Union[Resource, 'Project']] = Field(alias="cq:belongsToProject", default=None)
    
    belongstorelease: typing.Optional[typing.Union[Resource, 'Release']] = Field(alias="cq:belongsToRelease", default=None)
    
    belongstoreleaserelmap: typing.Optional[typing.Union[Resource, 'Releasereleasemap']] = Field(alias="cq:belongsToReleaserelmap", default=None)
    
    belongstoworkitem: typing.Optional[typing.Union[Resource, 'Workitem']] = Field(alias="cq:belongsToWorkitem", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    forms: typing.Optional[str] = Field(alias="cq:Forms", default=None)
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    historyid: typing.Optional[int] = Field(alias="cq:HistoryId", default=None)
    
    historylog: typing.Optional[str] = Field(alias="cq:HistoryLog", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    lastmodifieddate: typing.Optional[datetime] = Field(alias="cq:LastModifiedDate", default=None)
    
    lastmodifieduser: typing.Optional[str] = Field(alias="cq:LastModifiedUser", default=None)
    
    lifecyclestate: typing.Optional[str] = Field(alias="cq:LifeCycleState", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    migration: typing.Optional[str] = Field(alias="cq:Migration", default=None)
    
    operationcontext: typing.Optional[str] = Field(alias="cq:OperationContext", default=None)
    
    operationmode: typing.Optional[str] = Field(alias="cq:OperationMode", default=None)
    
    previouslifecyclestate: typing.Optional[str] = Field(alias="cq:PreviousLifeCycleState", default=None)
    
    ratl_forminfo: typing.Optional[str] = Field(alias="cq:ratl_forminfo", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    sourcerecordtype: typing.Optional[str] = Field(alias="cq:SourceRecordtype", default=None)
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    tld: typing.Optional[str] = Field(alias="cq:TLD", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16782410"

    @classmethod
    def shape_name(cls) -> str:
        return "HistoryLog"

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
    


class HistorylogProperty(Enum):
    actionname = "cq:ActionName"
    belongstocommercial = "cq:belongsToCommercial"
    belongstocontact = "cq:belongsToContact"
    belongstoexchangeprotocol = "cq:belongsToExchangeProtocol"
    belongstoissue = "cq:belongsToIssue"
    belongstoissueissuemap = "cq:belongsToIssueissuemap"
    belongstoissuereleasemap = "cq:belongsToIssuereleasemap"
    belongstoproblem = "cq:belongsToProblem"
    belongstoproject = "cq:belongsToProject"
    belongstorelease = "cq:belongsToRelease"
    belongstoreleaserelmap = "cq:belongsToReleaserelmap"
    belongstoworkitem = "cq:belongsToWorkitem"
    dbid = "cq:dbid"
    dcterms__type = "dcterms:type"
    forms = "cq:Forms"
    history = "cq:history"
    historyid = "cq:HistoryId"
    historylog = "cq:HistoryLog"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    is_active = "cq:is_active"
    lastmodifieddate = "cq:LastModifiedDate"
    lastmodifieduser = "cq:LastModifiedUser"
    lifecyclestate = "cq:LifeCycleState"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    migration = "cq:Migration"
    operationcontext = "cq:OperationContext"
    operationmode = "cq:OperationMode"
    previouslifecyclestate = "cq:PreviousLifeCycleState"
    ratl_forminfo = "cq:ratl_forminfo"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    sourcerecordtype = "cq:SourceRecordtype"
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


def create_historylog_changeset(
    actionname: typing.Optional[str] = None,
    belongstocommercial: typing.Optional[typing.Union[Resource, 'Commercial']] = None,
    belongstocontact: typing.Optional[typing.Union[Resource, 'Contact']] = None,
    belongstoexchangeprotocol: typing.Optional[typing.Union[Resource, 'Exchangeprotocol']] = None,
    belongstoissue: typing.Optional[typing.Union[Resource, 'Issue']] = None,
    belongstoissueissuemap: typing.Optional[typing.Union[Resource, 'Issueissuemap']] = None,
    belongstoissuereleasemap: typing.Optional[typing.Union[Resource, 'Issuereleasemap']] = None,
    belongstoproblem: typing.Optional[typing.Union[Resource, 'Problem']] = None,
    belongstoproject: typing.Optional[typing.Union[Resource, 'Project']] = None,
    belongstorelease: typing.Optional[typing.Union[Resource, 'Release']] = None,
    belongstoreleaserelmap: typing.Optional[typing.Union[Resource, 'Releasereleasemap']] = None,
    belongstoworkitem: typing.Optional[typing.Union[Resource, 'Workitem']] = None,
    dbid: typing.Optional[str] = None,
    forms: typing.Optional[str] = None,
    history: typing.Optional[Resource] = None,
    historyid: typing.Optional[int] = None,
    historylog: typing.Optional[str] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    is_active: typing.Optional[int] = None,
    lastmodifieddate: typing.Optional[datetime] = None,
    lastmodifieduser: typing.Optional[str] = None,
    lifecyclestate: typing.Optional[str] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    migration: typing.Optional[str] = None,
    operationcontext: typing.Optional[str] = None,
    operationmode: typing.Optional[str] = None,
    previouslifecyclestate: typing.Optional[str] = None,
    ratl_forminfo: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    sourcerecordtype: typing.Optional[str] = None,
    title: typing.Optional[str] = None,
    tld: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    
) -> Historylog:
    '''
    Create a Historylog changeset.
    '''
    fields = {}
    if actionname is not None:
        fields["cq:ActionName"] = actionname 
    if belongstocommercial is not None:
        fields["cq:belongsToCommercial"] = belongstocommercial 
    if belongstocontact is not None:
        fields["cq:belongsToContact"] = belongstocontact 
    if belongstoexchangeprotocol is not None:
        fields["cq:belongsToExchangeProtocol"] = belongstoexchangeprotocol 
    if belongstoissue is not None:
        fields["cq:belongsToIssue"] = belongstoissue 
    if belongstoissueissuemap is not None:
        fields["cq:belongsToIssueissuemap"] = belongstoissueissuemap 
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
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if forms is not None:
        fields["cq:Forms"] = forms 
    if history is not None:
        fields["cq:history"] = history 
    if historyid is not None:
        fields["cq:HistoryId"] = historyid 
    if historylog is not None:
        fields["cq:HistoryLog"] = historylog 
    if identifier is not None:
        fields["dcterms:identifier"] = identifier 
    if instanceshape is not None:
        fields["oslc:instanceShape"] = instanceshape 
    if is_active is not None:
        fields["cq:is_active"] = is_active 
    if lastmodifieddate is not None:
        fields["cq:LastModifiedDate"] = lastmodifieddate 
    if lastmodifieduser is not None:
        fields["cq:LastModifiedUser"] = lastmodifieduser 
    if lifecyclestate is not None:
        fields["cq:LifeCycleState"] = lifecyclestate 
    if lock_version is not None:
        fields["cq:lock_version"] = lock_version 
    if locked_by is not None:
        fields["cq:locked_by"] = locked_by 
    if migration is not None:
        fields["cq:Migration"] = migration 
    if operationcontext is not None:
        fields["cq:OperationContext"] = operationcontext 
    if operationmode is not None:
        fields["cq:OperationMode"] = operationmode 
    if previouslifecyclestate is not None:
        fields["cq:PreviousLifeCycleState"] = previouslifecyclestate 
    if ratl_forminfo is not None:
        fields["cq:ratl_forminfo"] = ratl_forminfo 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if sourcerecordtype is not None:
        fields["cq:SourceRecordtype"] = sourcerecordtype 
    if title is not None:
        fields["dcterms:title"] = title 
    if tld is not None:
        fields["cq:TLD"] = tld 
    if version is not None:
        fields["cq:version"] = version 
    

    fields["dcterms:type"] = "HistoryLog"
    return Historylog(**fields)