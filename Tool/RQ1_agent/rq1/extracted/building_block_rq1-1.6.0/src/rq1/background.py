
from .base import Resource, QueryStatement, BaseRecord
from pydantic import ConfigDict, Field, field_validator
import typing
from datetime import datetime, timezone
from enum import Enum
import json


from typing import TYPE_CHECKING

if TYPE_CHECKING:

    from .ratl_replicas import Ratl_Replicas

    from .users import Users



class Background(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    activitystatus: typing.Optional[str] = Field(alias="cq:ActivityStatus", default=None)
    
    aggregatedestimatedeffort: typing.Optional[str] = Field(alias="cq:AggregatedEstimatedEffort", default=None)
    
    aggregatedstatus: typing.Optional[str] = Field(alias="cq:AggregatedStatus", default=None)
    
    bestlifecyclestate: typing.Optional[str] = Field(alias="cq:BestLifeCycleState", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    defectid: typing.Optional[str] = Field(alias="cq:DefectID", default=None)
    
    flowtags: typing.Optional[str] = Field(alias="cq:FlowTags", default=None)
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    historylog: typing.Optional[str] = Field(alias="cq:HistoryLog", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    lastmodifieddate: typing.Optional[datetime] = Field(alias="cq:LastModifiedDate", default=None)
    
    lastmodifieduser: typing.Optional[str] = Field(alias="cq:LastModifiedUser", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    migration: typing.Optional[str] = Field(alias="cq:Migration", default=None)
    
    operationcontext: typing.Optional[str] = Field(alias="cq:OperationContext", default=None)
    
    operationmode: typing.Optional[str] = Field(alias="cq:OperationMode", default=None)
    
    primaryrootcause: typing.Optional[str] = Field(alias="cq:PrimaryRootCause", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    scmreferences: typing.Optional[str] = Field(alias="cq:SCMReferences", default=None)
    
    scmsystem: typing.Optional[str] = Field(alias="cq:SCMSystem", default=None)
    
    scmuserid: typing.Optional[str] = Field(alias="cq:SCMUserId", default=None)
    
    supplierbaselineid: typing.Optional[str] = Field(alias="cq:SupplierBaselineID", default=None)
    
    supplierid: typing.Optional[str] = Field(alias="cq:SupplierID", default=None)
    
    supplierissueid: typing.Optional[str] = Field(alias="cq:SupplierIssueID", default=None)
    
    supplierissuestate: typing.Optional[str] = Field(alias="cq:SupplierIssueState", default=None)
    
    supplierstate: typing.Optional[str] = Field(alias="cq:SupplierState", default=None)
    
    suppliersystem: typing.Optional[str] = Field(alias="cq:SupplierSystem", default=None)
    
    suppliertaskid: typing.Optional[str] = Field(alias="cq:SupplierTaskID", default=None)
    
    targetdbid: typing.Optional[int] = Field(alias="cq:TargetDbId", default=None)
    
    targetrecordtype: typing.Optional[str] = Field(alias="cq:TargetRecordType", default=None)
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    tld: typing.Optional[str] = Field(alias="cq:TLD", default=None)
    
    updatedescription: typing.Optional[str] = Field(alias="cq:UpdateDescription", default=None)
    
    updateflag: typing.Optional[str] = Field(alias="cq:UpdateFlag", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16777240"

    @classmethod
    def shape_name(cls) -> str:
        return "Background"

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
    


class BackgroundProperty(Enum):
    activitystatus = "cq:ActivityStatus"
    aggregatedestimatedeffort = "cq:AggregatedEstimatedEffort"
    aggregatedstatus = "cq:AggregatedStatus"
    bestlifecyclestate = "cq:BestLifeCycleState"
    dbid = "cq:dbid"
    dcterms__type = "dcterms:type"
    defectid = "cq:DefectID"
    flowtags = "cq:FlowTags"
    history = "cq:history"
    historylog = "cq:HistoryLog"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    is_active = "cq:is_active"
    lastmodifieddate = "cq:LastModifiedDate"
    lastmodifieduser = "cq:LastModifiedUser"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    migration = "cq:Migration"
    operationcontext = "cq:OperationContext"
    operationmode = "cq:OperationMode"
    primaryrootcause = "cq:PrimaryRootCause"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    scmreferences = "cq:SCMReferences"
    scmsystem = "cq:SCMSystem"
    scmuserid = "cq:SCMUserId"
    supplierbaselineid = "cq:SupplierBaselineID"
    supplierid = "cq:SupplierID"
    supplierissueid = "cq:SupplierIssueID"
    supplierissuestate = "cq:SupplierIssueState"
    supplierstate = "cq:SupplierState"
    suppliersystem = "cq:SupplierSystem"
    suppliertaskid = "cq:SupplierTaskID"
    targetdbid = "cq:TargetDbId"
    targetrecordtype = "cq:TargetRecordType"
    title = "dcterms:title"
    tld = "cq:TLD"
    updatedescription = "cq:UpdateDescription"
    updateflag = "cq:UpdateFlag"
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


def create_background_changeset(
    activitystatus: typing.Optional[str] = None,
    aggregatedestimatedeffort: typing.Optional[str] = None,
    aggregatedstatus: typing.Optional[str] = None,
    bestlifecyclestate: typing.Optional[str] = None,
    dbid: typing.Optional[str] = None,
    defectid: typing.Optional[str] = None,
    flowtags: typing.Optional[str] = None,
    history: typing.Optional[Resource] = None,
    historylog: typing.Optional[str] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    is_active: typing.Optional[int] = None,
    lastmodifieddate: typing.Optional[datetime] = None,
    lastmodifieduser: typing.Optional[str] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    migration: typing.Optional[str] = None,
    operationcontext: typing.Optional[str] = None,
    operationmode: typing.Optional[str] = None,
    primaryrootcause: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    scmreferences: typing.Optional[str] = None,
    scmsystem: typing.Optional[str] = None,
    scmuserid: typing.Optional[str] = None,
    supplierbaselineid: typing.Optional[str] = None,
    supplierid: typing.Optional[str] = None,
    supplierissueid: typing.Optional[str] = None,
    supplierissuestate: typing.Optional[str] = None,
    supplierstate: typing.Optional[str] = None,
    suppliersystem: typing.Optional[str] = None,
    suppliertaskid: typing.Optional[str] = None,
    targetdbid: typing.Optional[int] = None,
    targetrecordtype: typing.Optional[str] = None,
    title: typing.Optional[str] = None,
    tld: typing.Optional[str] = None,
    updatedescription: typing.Optional[str] = None,
    updateflag: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    
) -> Background:
    '''
    Create a Background changeset.
    '''
    fields = {}
    if activitystatus is not None:
        fields["cq:ActivityStatus"] = activitystatus 
    if aggregatedestimatedeffort is not None:
        fields["cq:AggregatedEstimatedEffort"] = aggregatedestimatedeffort 
    if aggregatedstatus is not None:
        fields["cq:AggregatedStatus"] = aggregatedstatus 
    if bestlifecyclestate is not None:
        fields["cq:BestLifeCycleState"] = bestlifecyclestate 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if defectid is not None:
        fields["cq:DefectID"] = defectid 
    if flowtags is not None:
        fields["cq:FlowTags"] = flowtags 
    if history is not None:
        fields["cq:history"] = history 
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
    if primaryrootcause is not None:
        fields["cq:PrimaryRootCause"] = primaryrootcause 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if scmreferences is not None:
        fields["cq:SCMReferences"] = scmreferences 
    if scmsystem is not None:
        fields["cq:SCMSystem"] = scmsystem 
    if scmuserid is not None:
        fields["cq:SCMUserId"] = scmuserid 
    if supplierbaselineid is not None:
        fields["cq:SupplierBaselineID"] = supplierbaselineid 
    if supplierid is not None:
        fields["cq:SupplierID"] = supplierid 
    if supplierissueid is not None:
        fields["cq:SupplierIssueID"] = supplierissueid 
    if supplierissuestate is not None:
        fields["cq:SupplierIssueState"] = supplierissuestate 
    if supplierstate is not None:
        fields["cq:SupplierState"] = supplierstate 
    if suppliersystem is not None:
        fields["cq:SupplierSystem"] = suppliersystem 
    if suppliertaskid is not None:
        fields["cq:SupplierTaskID"] = suppliertaskid 
    if targetdbid is not None:
        fields["cq:TargetDbId"] = targetdbid 
    if targetrecordtype is not None:
        fields["cq:TargetRecordType"] = targetrecordtype 
    if title is not None:
        fields["dcterms:title"] = title 
    if tld is not None:
        fields["cq:TLD"] = tld 
    if updatedescription is not None:
        fields["cq:UpdateDescription"] = updatedescription 
    if updateflag is not None:
        fields["cq:UpdateFlag"] = updateflag 
    if version is not None:
        fields["cq:version"] = version 
    

    fields["dcterms:type"] = "Background"
    return Background(**fields)