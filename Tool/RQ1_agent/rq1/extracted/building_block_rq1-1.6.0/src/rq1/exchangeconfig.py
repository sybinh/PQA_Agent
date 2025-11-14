
from .base import Resource, QueryStatement, BaseRecord
from pydantic import ConfigDict, Field, field_validator
import typing
from datetime import datetime, timezone
from enum import Enum
import json


from typing import TYPE_CHECKING

if TYPE_CHECKING:

    from .ratl_replicas import Ratl_Replicas

    from .project import Project

    from .users import Users



class Exchangeconfig(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    belongstoproject: typing.Optional[typing.Union[Resource, 'Project']] = Field(alias="cq:belongsToProject", default=None)
    
    belongsttomassagestates: typing.List[typing.Union[Resource, 'Exchangeconfig']] = Field(alias="cq:belongstToMassageStates", default_factory=lambda: [])
    
    cq__Title: typing.Optional[str] = Field(alias="cq:Title", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    exchangeformat: typing.Optional[str] = Field(alias="cq:ExchangeFormat", default=None)
    
    exportvalue: typing.Optional[str] = Field(alias="cq:ExportValue", default=None)
    
    ftp_path: typing.Optional[str] = Field(alias="cq:FTP_Path", default=None)
    
    ftp_pwd: typing.Optional[str] = Field(alias="cq:FTP_Pwd", default=None)
    
    ftp_server: typing.Optional[str] = Field(alias="cq:FTP_Server", default=None)
    
    ftp_user: typing.Optional[str] = Field(alias="cq:FTP_User", default=None)
    
    hasmappings: typing.List[typing.Union[Resource, 'Exchangeconfig']] = Field(alias="cq:hasMappings", default_factory=lambda: [])
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    importexport: typing.Optional[str] = Field(alias="cq:ImportExport", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    isattachment: typing.Optional[str] = Field(alias="cq:isAttachment", default=None)
    
    isenabled: typing.Optional[str] = Field(alias="cq:isEnabled", default=None)
    
    isfileaccess: typing.Optional[str] = Field(alias="cq:isFileAccess", default=None)
    
    isftp: typing.Optional[str] = Field(alias="cq:isFTP", default=None)
    
    iskey: typing.Optional[str] = Field(alias="cq:isKey", default=None)
    
    ismandatory: typing.Optional[str] = Field(alias="cq:isMandatory", default=None)
    
    isreference: typing.Optional[str] = Field(alias="cq:isReference", default=None)
    
    isroot: typing.Optional[str] = Field(alias="cq:isRoot", default=None)
    
    isxpath: typing.Optional[str] = Field(alias="cq:isXPath", default=None)
    
    lastmodifieddate: typing.Optional[datetime] = Field(alias="cq:LastModifiedDate", default=None)
    
    lastmodifieduser: typing.Optional[str] = Field(alias="cq:LastModifiedUser", default=None)
    
    localpath: typing.Optional[str] = Field(alias="cq:LocalPath", default=None)
    
    localsource: typing.Optional[str] = Field(alias="cq:LocalSource", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    mainvalue: typing.Optional[str] = Field(alias="cq:mainValue", default=None)
    
    messagestate: typing.Optional[str] = Field(alias="cq:MessageState", default=None)
    
    overwrite: typing.Optional[str] = Field(alias="cq:Overwrite", default=None)
    
    postvalue: typing.Optional[str] = Field(alias="cq:postValue", default=None)
    
    prevalue: typing.Optional[str] = Field(alias="cq:preValue", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    recordtypecontrol: typing.Optional[str] = Field(alias="cq:RecordTypeControl", default=None)
    
    releasecommercialnote: typing.Optional[str] = Field(alias="cq:releaseCommercialNote", default=None)
    
    releasetechnicalnote: typing.Optional[str] = Field(alias="cq:releaseTechnicalNote", default=None)
    
    requestonefield: typing.Optional[str] = Field(alias="cq:RequestOneField", default=None)
    
    shorttitle: typing.Optional[str] = Field(alias="oslc:shortTitle", default=None)
    
    tld: typing.Optional[str] = Field(alias="cq:TLD", default=None)
    
    updateexternalstate: typing.Optional[str] = Field(alias="cq:updateExternalState", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16777244"

    @classmethod
    def shape_name(cls) -> str:
        return "ExchangeConfig"

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
    


class ExchangeconfigProperty(Enum):
    belongstoproject = "cq:belongsToProject"
    belongsttomassagestates = "cq:belongstToMassageStates"
    cq__Title = "cq:Title"
    dbid = "cq:dbid"
    dcterms__title = "dcterms:title"
    dcterms__type = "dcterms:type"
    exchangeformat = "cq:ExchangeFormat"
    exportvalue = "cq:ExportValue"
    ftp_path = "cq:FTP_Path"
    ftp_pwd = "cq:FTP_Pwd"
    ftp_server = "cq:FTP_Server"
    ftp_user = "cq:FTP_User"
    hasmappings = "cq:hasMappings"
    history = "cq:history"
    identifier = "dcterms:identifier"
    importexport = "cq:ImportExport"
    instanceshape = "oslc:instanceShape"
    is_active = "cq:is_active"
    isattachment = "cq:isAttachment"
    isenabled = "cq:isEnabled"
    isfileaccess = "cq:isFileAccess"
    isftp = "cq:isFTP"
    iskey = "cq:isKey"
    ismandatory = "cq:isMandatory"
    isreference = "cq:isReference"
    isroot = "cq:isRoot"
    isxpath = "cq:isXPath"
    lastmodifieddate = "cq:LastModifiedDate"
    lastmodifieduser = "cq:LastModifiedUser"
    localpath = "cq:LocalPath"
    localsource = "cq:LocalSource"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    mainvalue = "cq:mainValue"
    messagestate = "cq:MessageState"
    overwrite = "cq:Overwrite"
    postvalue = "cq:postValue"
    prevalue = "cq:preValue"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    recordtypecontrol = "cq:RecordTypeControl"
    releasecommercialnote = "cq:releaseCommercialNote"
    releasetechnicalnote = "cq:releaseTechnicalNote"
    requestonefield = "cq:RequestOneField"
    shorttitle = "oslc:shortTitle"
    tld = "cq:TLD"
    updateexternalstate = "cq:updateExternalState"
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


def create_exchangeconfig_changeset(
    belongstoproject: typing.Optional[typing.Union[Resource, 'Project']] = None,
    belongsttomassagestates: typing.Optional[typing.List[typing.Union[Resource, 'Exchangeconfig']]] = None,
    cq__Title: typing.Optional[str] = None,
    dbid: typing.Optional[str] = None,
    dcterms__title: typing.Optional[str] = None,
    exchangeformat: typing.Optional[str] = None,
    exportvalue: typing.Optional[str] = None,
    ftp_path: typing.Optional[str] = None,
    ftp_pwd: typing.Optional[str] = None,
    ftp_server: typing.Optional[str] = None,
    ftp_user: typing.Optional[str] = None,
    hasmappings: typing.Optional[typing.List[typing.Union[Resource, 'Exchangeconfig']]] = None,
    history: typing.Optional[Resource] = None,
    identifier: typing.Optional[str] = None,
    importexport: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    is_active: typing.Optional[int] = None,
    isattachment: typing.Optional[str] = None,
    isenabled: typing.Optional[str] = None,
    isfileaccess: typing.Optional[str] = None,
    isftp: typing.Optional[str] = None,
    iskey: typing.Optional[str] = None,
    ismandatory: typing.Optional[str] = None,
    isreference: typing.Optional[str] = None,
    isroot: typing.Optional[str] = None,
    isxpath: typing.Optional[str] = None,
    lastmodifieddate: typing.Optional[datetime] = None,
    lastmodifieduser: typing.Optional[str] = None,
    localpath: typing.Optional[str] = None,
    localsource: typing.Optional[str] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    mainvalue: typing.Optional[str] = None,
    messagestate: typing.Optional[str] = None,
    overwrite: typing.Optional[str] = None,
    postvalue: typing.Optional[str] = None,
    prevalue: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    recordtypecontrol: typing.Optional[str] = None,
    releasecommercialnote: typing.Optional[str] = None,
    releasetechnicalnote: typing.Optional[str] = None,
    requestonefield: typing.Optional[str] = None,
    shorttitle: typing.Optional[str] = None,
    tld: typing.Optional[str] = None,
    updateexternalstate: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    
) -> Exchangeconfig:
    '''
    Create a Exchangeconfig changeset.
    '''
    fields = {}
    if belongstoproject is not None:
        fields["cq:belongsToProject"] = belongstoproject 
    if belongsttomassagestates is not None:
        fields["cq:belongstToMassageStates"] = belongsttomassagestates 
    if cq__Title is not None:
        fields["cq:Title"] = cq__Title 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if dcterms__title is not None:
        fields["dcterms:title"] = dcterms__title 
    if exchangeformat is not None:
        fields["cq:ExchangeFormat"] = exchangeformat 
    if exportvalue is not None:
        fields["cq:ExportValue"] = exportvalue 
    if ftp_path is not None:
        fields["cq:FTP_Path"] = ftp_path 
    if ftp_pwd is not None:
        fields["cq:FTP_Pwd"] = ftp_pwd 
    if ftp_server is not None:
        fields["cq:FTP_Server"] = ftp_server 
    if ftp_user is not None:
        fields["cq:FTP_User"] = ftp_user 
    if hasmappings is not None:
        fields["cq:hasMappings"] = hasmappings 
    if history is not None:
        fields["cq:history"] = history 
    if identifier is not None:
        fields["dcterms:identifier"] = identifier 
    if importexport is not None:
        fields["cq:ImportExport"] = importexport 
    if instanceshape is not None:
        fields["oslc:instanceShape"] = instanceshape 
    if is_active is not None:
        fields["cq:is_active"] = is_active 
    if isattachment is not None:
        fields["cq:isAttachment"] = isattachment 
    if isenabled is not None:
        fields["cq:isEnabled"] = isenabled 
    if isfileaccess is not None:
        fields["cq:isFileAccess"] = isfileaccess 
    if isftp is not None:
        fields["cq:isFTP"] = isftp 
    if iskey is not None:
        fields["cq:isKey"] = iskey 
    if ismandatory is not None:
        fields["cq:isMandatory"] = ismandatory 
    if isreference is not None:
        fields["cq:isReference"] = isreference 
    if isroot is not None:
        fields["cq:isRoot"] = isroot 
    if isxpath is not None:
        fields["cq:isXPath"] = isxpath 
    if lastmodifieddate is not None:
        fields["cq:LastModifiedDate"] = lastmodifieddate 
    if lastmodifieduser is not None:
        fields["cq:LastModifiedUser"] = lastmodifieduser 
    if localpath is not None:
        fields["cq:LocalPath"] = localpath 
    if localsource is not None:
        fields["cq:LocalSource"] = localsource 
    if lock_version is not None:
        fields["cq:lock_version"] = lock_version 
    if locked_by is not None:
        fields["cq:locked_by"] = locked_by 
    if mainvalue is not None:
        fields["cq:mainValue"] = mainvalue 
    if messagestate is not None:
        fields["cq:MessageState"] = messagestate 
    if overwrite is not None:
        fields["cq:Overwrite"] = overwrite 
    if postvalue is not None:
        fields["cq:postValue"] = postvalue 
    if prevalue is not None:
        fields["cq:preValue"] = prevalue 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if recordtypecontrol is not None:
        fields["cq:RecordTypeControl"] = recordtypecontrol 
    if releasecommercialnote is not None:
        fields["cq:releaseCommercialNote"] = releasecommercialnote 
    if releasetechnicalnote is not None:
        fields["cq:releaseTechnicalNote"] = releasetechnicalnote 
    if requestonefield is not None:
        fields["cq:RequestOneField"] = requestonefield 
    if shorttitle is not None:
        fields["oslc:shortTitle"] = shorttitle 
    if tld is not None:
        fields["cq:TLD"] = tld 
    if updateexternalstate is not None:
        fields["cq:updateExternalState"] = updateexternalstate 
    if version is not None:
        fields["cq:version"] = version 
    

    fields["dcterms:type"] = "ExchangeConfig"
    return Exchangeconfig(**fields)