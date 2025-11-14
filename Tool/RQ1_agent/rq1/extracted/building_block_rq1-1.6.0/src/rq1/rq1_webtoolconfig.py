
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



class Rq1_Webtoolconfig(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    configdata: typing.Optional[str] = Field(alias="cq:ConfigData", default=None)
    
    cq__Description: typing.Optional[str] = Field(alias="cq:Description", default=None)
    
    cq__Status: typing.Optional[typing.Literal["disabled", "enabled"]] = Field(alias="cq:Status", default=None)
    
    datecategory: typing.Optional[str] = Field(alias="cq:DateCategory", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__description: typing.Optional[str] = Field(alias="dcterms:description", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    display_hint: typing.Optional[str] = Field(alias="cq:Display_Hint", default=None)
    
    download_url: typing.Optional[str] = Field(alias="cq:Download_URL", default=None)
    
    enabled: typing.Optional[int] = Field(alias="cq:Enabled", default=None)
    
    engine_name: typing.Optional[str] = Field(alias="cq:Engine_Name", default=None)
    
    engine_version: typing.Optional[str] = Field(alias="cq:Engine_Version", default=None)
    
    execute: typing.Optional[str] = Field(alias="cq:Execute", default=None)
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    module: typing.Optional[str] = Field(alias="cq:Module", default=None)
    
    oslc_cm__status: typing.Optional[str] = Field(alias="oslc_cm:status", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    responsible: typing.List[typing.Union[Resource, 'Users']] = Field(alias="cq:Responsible", default_factory=lambda: [])
    
    result: typing.Optional[str] = Field(alias="cq:Result", default=None)
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    utility_name: typing.Optional[str] = Field(alias="cq:Utility_Name", default=None)
    
    utility_version: typing.Optional[str] = Field(alias="cq:Utility_version", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    
    webowner: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:WebOwner", default=None)
    
    webversion: typing.Optional[str] = Field(alias="cq:WebVersion", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16782860"

    @classmethod
    def shape_name(cls) -> str:
        return "RQ1_WebToolConfig"

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
    


class Rq1_WebtoolconfigProperty(Enum):
    configdata = "cq:ConfigData"
    cq__Description = "cq:Description"
    cq__Status = "cq:Status"
    datecategory = "cq:DateCategory"
    dbid = "cq:dbid"
    dcterms__description = "dcterms:description"
    dcterms__type = "dcterms:type"
    display_hint = "cq:Display_Hint"
    download_url = "cq:Download_URL"
    enabled = "cq:Enabled"
    engine_name = "cq:Engine_Name"
    engine_version = "cq:Engine_Version"
    execute = "cq:Execute"
    history = "cq:history"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    is_active = "cq:is_active"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    module = "cq:Module"
    oslc_cm__status = "oslc_cm:status"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    responsible = "cq:Responsible"
    result = "cq:Result"
    title = "dcterms:title"
    utility_name = "cq:Utility_Name"
    utility_version = "cq:Utility_version"
    version = "cq:version"
    webowner = "cq:WebOwner"
    webversion = "cq:WebVersion"
    

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


def create_rq1_webtoolconfig_changeset(
    configdata: typing.Optional[str] = None,
    cq__Description: typing.Optional[str] = None,
    cq__Status: typing.Optional[typing.Literal["disabled", "enabled"]] = None,
    datecategory: typing.Optional[str] = None,
    dbid: typing.Optional[str] = None,
    dcterms__description: typing.Optional[str] = None,
    display_hint: typing.Optional[str] = None,
    download_url: typing.Optional[str] = None,
    enabled: typing.Optional[int] = None,
    engine_name: typing.Optional[str] = None,
    engine_version: typing.Optional[str] = None,
    execute: typing.Optional[str] = None,
    history: typing.Optional[Resource] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    is_active: typing.Optional[int] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    module: typing.Optional[str] = None,
    oslc_cm__status: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    responsible: typing.Optional[typing.List[typing.Union[Resource, 'Users']]] = None,
    result: typing.Optional[str] = None,
    title: typing.Optional[str] = None,
    utility_name: typing.Optional[str] = None,
    utility_version: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    webowner: typing.Optional[typing.Union[Resource, 'Users']] = None,
    webversion: typing.Optional[str] = None,
    
) -> Rq1_Webtoolconfig:
    '''
    Create a Rq1_Webtoolconfig changeset.
    '''
    fields = {}
    if configdata is not None:
        fields["cq:ConfigData"] = configdata 
    if cq__Description is not None:
        fields["cq:Description"] = cq__Description 
    if cq__Status is not None:
        fields["cq:Status"] = cq__Status 
    if datecategory is not None:
        fields["cq:DateCategory"] = datecategory 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if dcterms__description is not None:
        fields["dcterms:description"] = dcterms__description 
    if display_hint is not None:
        fields["cq:Display_Hint"] = display_hint 
    if download_url is not None:
        fields["cq:Download_URL"] = download_url 
    if enabled is not None:
        fields["cq:Enabled"] = enabled 
    if engine_name is not None:
        fields["cq:Engine_Name"] = engine_name 
    if engine_version is not None:
        fields["cq:Engine_Version"] = engine_version 
    if execute is not None:
        fields["cq:Execute"] = execute 
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
    if module is not None:
        fields["cq:Module"] = module 
    if oslc_cm__status is not None:
        fields["oslc_cm:status"] = oslc_cm__status 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if responsible is not None:
        fields["cq:Responsible"] = responsible 
    if result is not None:
        fields["cq:Result"] = result 
    if title is not None:
        fields["dcterms:title"] = title 
    if utility_name is not None:
        fields["cq:Utility_Name"] = utility_name 
    if utility_version is not None:
        fields["cq:Utility_version"] = utility_version 
    if version is not None:
        fields["cq:version"] = version 
    if webowner is not None:
        fields["cq:WebOwner"] = webowner 
    if webversion is not None:
        fields["cq:WebVersion"] = webversion 
    

    fields["dcterms:type"] = "RQ1_WebToolConfig"
    return Rq1_Webtoolconfig(**fields)