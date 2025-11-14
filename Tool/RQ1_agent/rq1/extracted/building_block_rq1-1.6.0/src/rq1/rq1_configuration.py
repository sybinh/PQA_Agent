
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



class Rq1_Configuration(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    cfgversioninstructiontype: typing.Optional[str] = Field(alias="cq:CFGVersionInstructionType", default=None)
    
    cfgversionmetadata: typing.Optional[str] = Field(alias="cq:CFGVersionMetaData", default=None)
    
    cfgversionrolespermission: typing.Optional[str] = Field(alias="cq:CFGVersionRolesPermission", default=None)
    
    cfgversionrulesconfig: typing.Optional[str] = Field(alias="cq:CFGVersionRulesConfig", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    extendedvalue: typing.Optional[str] = Field(alias="cq:ExtendedValue", default=None)
    
    hasacl: typing.Optional[typing.Union[Resource, 'Commercialacl']] = Field(alias="cq:hasACL", default=None)
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    lastmodifieddate: typing.Optional[datetime] = Field(alias="cq:LastModifiedDate", default=None)
    
    lastmodifieduser: typing.Optional[str] = Field(alias="cq:LastModifiedUser", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    mailvalue: typing.Optional[str] = Field(alias="cq:MailValue", default=None)
    
    name: typing.Optional[str] = Field(alias="cq:Name", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    revision: typing.Optional[int] = Field(alias="cq:Revision", default=None)
    
    shorttitle: typing.Optional[str] = Field(alias="oslc:shortTitle", default=None)
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    tld: typing.Optional[str] = Field(alias="cq:TLD", default=None)
    
    value: typing.Optional[str] = Field(alias="cq:Value", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16781801"

    @classmethod
    def shape_name(cls) -> str:
        return "RQ1_Configuration"

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
    


class Rq1_ConfigurationProperty(Enum):
    cfgversioninstructiontype = "cq:CFGVersionInstructionType"
    cfgversionmetadata = "cq:CFGVersionMetaData"
    cfgversionrolespermission = "cq:CFGVersionRolesPermission"
    cfgversionrulesconfig = "cq:CFGVersionRulesConfig"
    dbid = "cq:dbid"
    dcterms__type = "dcterms:type"
    extendedvalue = "cq:ExtendedValue"
    hasacl = "cq:hasACL"
    history = "cq:history"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    is_active = "cq:is_active"
    lastmodifieddate = "cq:LastModifiedDate"
    lastmodifieduser = "cq:LastModifiedUser"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    mailvalue = "cq:MailValue"
    name = "cq:Name"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
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


def create_rq1_configuration_changeset(
    cfgversioninstructiontype: typing.Optional[str] = None,
    cfgversionmetadata: typing.Optional[str] = None,
    cfgversionrolespermission: typing.Optional[str] = None,
    cfgversionrulesconfig: typing.Optional[str] = None,
    dbid: typing.Optional[str] = None,
    extendedvalue: typing.Optional[str] = None,
    hasacl: typing.Optional[typing.Union[Resource, 'Commercialacl']] = None,
    history: typing.Optional[Resource] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    is_active: typing.Optional[int] = None,
    lastmodifieddate: typing.Optional[datetime] = None,
    lastmodifieduser: typing.Optional[str] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    mailvalue: typing.Optional[str] = None,
    name: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    revision: typing.Optional[int] = None,
    shorttitle: typing.Optional[str] = None,
    title: typing.Optional[str] = None,
    tld: typing.Optional[str] = None,
    value: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    
) -> Rq1_Configuration:
    '''
    Create a Rq1_Configuration changeset.
    '''
    fields = {}
    if cfgversioninstructiontype is not None:
        fields["cq:CFGVersionInstructionType"] = cfgversioninstructiontype 
    if cfgversionmetadata is not None:
        fields["cq:CFGVersionMetaData"] = cfgversionmetadata 
    if cfgversionrolespermission is not None:
        fields["cq:CFGVersionRolesPermission"] = cfgversionrolespermission 
    if cfgversionrulesconfig is not None:
        fields["cq:CFGVersionRulesConfig"] = cfgversionrulesconfig 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if extendedvalue is not None:
        fields["cq:ExtendedValue"] = extendedvalue 
    if hasacl is not None:
        fields["cq:hasACL"] = hasacl 
    if history is not None:
        fields["cq:history"] = history 
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
    if mailvalue is not None:
        fields["cq:MailValue"] = mailvalue 
    if name is not None:
        fields["cq:Name"] = name 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
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
    

    fields["dcterms:type"] = "RQ1_Configuration"
    return Rq1_Configuration(**fields)