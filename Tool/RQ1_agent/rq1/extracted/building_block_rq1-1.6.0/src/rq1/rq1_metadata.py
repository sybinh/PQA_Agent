
from .base import Resource, QueryStatement, BaseRecord
from pydantic import ConfigDict, Field, field_validator
import typing
from datetime import datetime, timezone
from enum import Enum
import json


from typing import TYPE_CHECKING

if TYPE_CHECKING:

    from .ratl_replicas import Ratl_Replicas

    from .commercialacl import Commercialacl

    from .users import Users



class Rq1_Metadata(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    cfgversion: typing.Optional[str] = Field(alias="cq:CFGVersion", default=None)
    
    changeable: typing.Optional[int] = Field(alias="cq:Changeable", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    developerid: typing.Optional[str] = Field(alias="cq:DeveloperID", default=None)
    
    enabled: typing.Optional[typing.Literal["0", "1"]] = Field(alias="cq:Enabled", default=None)
    
    encrypt: typing.Optional[typing.Literal["0", "1"]] = Field(alias="cq:Encrypt", default=None)
    
    hasacl: typing.Optional[typing.Union[Resource, 'Commercialacl']] = Field(alias="cq:hasACL", default=None)
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    historylog: typing.Optional[str] = Field(alias="cq:HistoryLog", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    lastmodifieddate: typing.Optional[datetime] = Field(alias="cq:LastModifiedDate", default=None)
    
    lastmodifieduser: typing.Optional[str] = Field(alias="cq:LastModifiedUser", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    name: typing.Optional[str] = Field(alias="cq:Name", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    remarks: typing.Optional[str] = Field(alias="cq:Remarks", default=None)
    
    revision: typing.Optional[int] = Field(alias="cq:Revision", default=None)
    
    shorttitle: typing.Optional[str] = Field(alias="oslc:shortTitle", default=None)
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    tld: typing.Optional[str] = Field(alias="cq:TLD", default=None)
    
    value: typing.Optional[str] = Field(alias="cq:Value", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16777238"

    @classmethod
    def shape_name(cls) -> str:
        return "RQ1_Metadata"

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
    


class Rq1_MetadataProperty(Enum):
    cfgversion = "cq:CFGVersion"
    changeable = "cq:Changeable"
    dbid = "cq:dbid"
    dcterms__type = "dcterms:type"
    developerid = "cq:DeveloperID"
    enabled = "cq:Enabled"
    encrypt = "cq:Encrypt"
    hasacl = "cq:hasACL"
    history = "cq:history"
    historylog = "cq:HistoryLog"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    is_active = "cq:is_active"
    lastmodifieddate = "cq:LastModifiedDate"
    lastmodifieduser = "cq:LastModifiedUser"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    name = "cq:Name"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    remarks = "cq:Remarks"
    revision = "cq:Revision"
    shorttitle = "oslc:shortTitle"
    title = "dcterms:title"
    tld = "cq:TLD"
    value = "cq:Value"
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


def create_rq1_metadata_changeset(
    cfgversion: typing.Optional[str] = None,
    changeable: typing.Optional[int] = None,
    dbid: typing.Optional[str] = None,
    developerid: typing.Optional[str] = None,
    enabled: typing.Optional[typing.Literal["0", "1"]] = None,
    encrypt: typing.Optional[typing.Literal["0", "1"]] = None,
    hasacl: typing.Optional[typing.Union[Resource, 'Commercialacl']] = None,
    history: typing.Optional[Resource] = None,
    historylog: typing.Optional[str] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    is_active: typing.Optional[int] = None,
    lastmodifieddate: typing.Optional[datetime] = None,
    lastmodifieduser: typing.Optional[str] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    name: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    remarks: typing.Optional[str] = None,
    revision: typing.Optional[int] = None,
    shorttitle: typing.Optional[str] = None,
    title: typing.Optional[str] = None,
    tld: typing.Optional[str] = None,
    value: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    
) -> Rq1_Metadata:
    '''
    Create a Rq1_Metadata changeset.
    '''
    fields = {}
    if cfgversion is not None:
        fields["cq:CFGVersion"] = cfgversion 
    if changeable is not None:
        fields["cq:Changeable"] = changeable 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if developerid is not None:
        fields["cq:DeveloperID"] = developerid 
    if enabled is not None:
        fields["cq:Enabled"] = enabled 
    if encrypt is not None:
        fields["cq:Encrypt"] = encrypt 
    if hasacl is not None:
        fields["cq:hasACL"] = hasacl 
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
    if name is not None:
        fields["cq:Name"] = name 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if remarks is not None:
        fields["cq:Remarks"] = remarks 
    if revision is not None:
        fields["cq:Revision"] = revision 
    if shorttitle is not None:
        fields["oslc:shortTitle"] = shorttitle 
    if title is not None:
        fields["dcterms:title"] = title 
    if tld is not None:
        fields["cq:TLD"] = tld 
    if value is not None:
        fields["cq:Value"] = value 
    if version is not None:
        fields["cq:version"] = version 
    

    fields["dcterms:type"] = "RQ1_Metadata"
    return Rq1_Metadata(**fields)