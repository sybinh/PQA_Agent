
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



class Mapping(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    callerdisplayname: typing.Optional[str] = Field(alias="cq:CallerDisplayName", default=None)
    
    callerrecordtype: typing.Optional[str] = Field(alias="cq:CallerRecordType", default=None)
    
    creationmode: typing.Optional[str] = Field(alias="cq:CreationMode", default=None)
    
    currentmappings: typing.Optional[str] = Field(alias="cq:CurrentMappings", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    mapcontext: typing.Optional[str] = Field(alias="cq:MapContext", default=None)
    
    mapfrom: typing.Optional[str] = Field(alias="cq:MapFrom", default=None)
    
    mapfromcategory: typing.Optional[str] = Field(alias="cq:MapFromCategory", default=None)
    
    mapfromdomain: typing.Optional[str] = Field(alias="cq:MapFromDomain", default=None)
    
    mapfromfilter: typing.Optional[str] = Field(alias="cq:MapFromFilter", default=None)
    
    mapfromlifecyclestate: typing.Optional[str] = Field(alias="cq:MapFromLifeCycleState", default=None)
    
    mapfromsearch: typing.Optional[str] = Field(alias="cq:MapFromSearch", default=None)
    
    mapfromtemp: typing.Optional[str] = Field(alias="cq:MapFromTemp", default=None)
    
    mapfromtype: typing.Optional[str] = Field(alias="cq:MapFromType", default=None)
    
    mapresultinfo: typing.Optional[str] = Field(alias="cq:MapResultInfo", default=None)
    
    mapresults: typing.Optional[str] = Field(alias="cq:MapResults", default=None)
    
    maptemp: typing.Optional[str] = Field(alias="cq:MapTemp", default=None)
    
    mapto: typing.Optional[str] = Field(alias="cq:MapTo", default=None)
    
    maptofilter: typing.Optional[str] = Field(alias="cq:MapToFilter", default=None)
    
    maptosearch: typing.Optional[str] = Field(alias="cq:MapToSearch", default=None)
    
    maptotemp: typing.Optional[str] = Field(alias="cq:MapToTemp", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    tld: typing.Optional[str] = Field(alias="cq:TLD", default=None)
    
    utilities: typing.Optional[str] = Field(alias="cq:Utilities", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16782645"

    @classmethod
    def shape_name(cls) -> str:
        return "Mapping"

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
    


class MappingProperty(Enum):
    callerdisplayname = "cq:CallerDisplayName"
    callerrecordtype = "cq:CallerRecordType"
    creationmode = "cq:CreationMode"
    currentmappings = "cq:CurrentMappings"
    dbid = "cq:dbid"
    dcterms__type = "dcterms:type"
    history = "cq:history"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    is_active = "cq:is_active"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    mapcontext = "cq:MapContext"
    mapfrom = "cq:MapFrom"
    mapfromcategory = "cq:MapFromCategory"
    mapfromdomain = "cq:MapFromDomain"
    mapfromfilter = "cq:MapFromFilter"
    mapfromlifecyclestate = "cq:MapFromLifeCycleState"
    mapfromsearch = "cq:MapFromSearch"
    mapfromtemp = "cq:MapFromTemp"
    mapfromtype = "cq:MapFromType"
    mapresultinfo = "cq:MapResultInfo"
    mapresults = "cq:MapResults"
    maptemp = "cq:MapTemp"
    mapto = "cq:MapTo"
    maptofilter = "cq:MapToFilter"
    maptosearch = "cq:MapToSearch"
    maptotemp = "cq:MapToTemp"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    title = "dcterms:title"
    tld = "cq:TLD"
    utilities = "cq:Utilities"
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


def create_mapping_changeset(
    callerdisplayname: typing.Optional[str] = None,
    callerrecordtype: typing.Optional[str] = None,
    creationmode: typing.Optional[str] = None,
    currentmappings: typing.Optional[str] = None,
    dbid: typing.Optional[str] = None,
    history: typing.Optional[Resource] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    is_active: typing.Optional[int] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    mapcontext: typing.Optional[str] = None,
    mapfrom: typing.Optional[str] = None,
    mapfromcategory: typing.Optional[str] = None,
    mapfromdomain: typing.Optional[str] = None,
    mapfromfilter: typing.Optional[str] = None,
    mapfromlifecyclestate: typing.Optional[str] = None,
    mapfromsearch: typing.Optional[str] = None,
    mapfromtemp: typing.Optional[str] = None,
    mapfromtype: typing.Optional[str] = None,
    mapresultinfo: typing.Optional[str] = None,
    mapresults: typing.Optional[str] = None,
    maptemp: typing.Optional[str] = None,
    mapto: typing.Optional[str] = None,
    maptofilter: typing.Optional[str] = None,
    maptosearch: typing.Optional[str] = None,
    maptotemp: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    title: typing.Optional[str] = None,
    tld: typing.Optional[str] = None,
    utilities: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    
) -> Mapping:
    '''
    Create a Mapping changeset.
    '''
    fields = {}
    if callerdisplayname is not None:
        fields["cq:CallerDisplayName"] = callerdisplayname 
    if callerrecordtype is not None:
        fields["cq:CallerRecordType"] = callerrecordtype 
    if creationmode is not None:
        fields["cq:CreationMode"] = creationmode 
    if currentmappings is not None:
        fields["cq:CurrentMappings"] = currentmappings 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if history is not None:
        fields["cq:history"] = history 
    if identifier is not None:
        fields["dcterms:identifier"] = identifier 
    if instanceshape is not None:
        fields["oslc:instanceShape"] = instanceshape 
    if is_active is not None:
        fields["cq:is_active"] = is_active 
    if lock_version is not None:
        fields["cq:lock_version"] = lock_version 
    if locked_by is not None:
        fields["cq:locked_by"] = locked_by 
    if mapcontext is not None:
        fields["cq:MapContext"] = mapcontext 
    if mapfrom is not None:
        fields["cq:MapFrom"] = mapfrom 
    if mapfromcategory is not None:
        fields["cq:MapFromCategory"] = mapfromcategory 
    if mapfromdomain is not None:
        fields["cq:MapFromDomain"] = mapfromdomain 
    if mapfromfilter is not None:
        fields["cq:MapFromFilter"] = mapfromfilter 
    if mapfromlifecyclestate is not None:
        fields["cq:MapFromLifeCycleState"] = mapfromlifecyclestate 
    if mapfromsearch is not None:
        fields["cq:MapFromSearch"] = mapfromsearch 
    if mapfromtemp is not None:
        fields["cq:MapFromTemp"] = mapfromtemp 
    if mapfromtype is not None:
        fields["cq:MapFromType"] = mapfromtype 
    if mapresultinfo is not None:
        fields["cq:MapResultInfo"] = mapresultinfo 
    if mapresults is not None:
        fields["cq:MapResults"] = mapresults 
    if maptemp is not None:
        fields["cq:MapTemp"] = maptemp 
    if mapto is not None:
        fields["cq:MapTo"] = mapto 
    if maptofilter is not None:
        fields["cq:MapToFilter"] = maptofilter 
    if maptosearch is not None:
        fields["cq:MapToSearch"] = maptosearch 
    if maptotemp is not None:
        fields["cq:MapToTemp"] = maptotemp 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if title is not None:
        fields["dcterms:title"] = title 
    if tld is not None:
        fields["cq:TLD"] = tld 
    if utilities is not None:
        fields["cq:Utilities"] = utilities 
    if version is not None:
        fields["cq:version"] = version 
    

    fields["dcterms:type"] = "Mapping"
    return Mapping(**fields)