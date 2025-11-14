
from .base import Resource, QueryStatement, BaseRecord
from pydantic import ConfigDict, Field, field_validator
import typing
from datetime import datetime, timezone
from enum import Enum
import json


from typing import TYPE_CHECKING

if TYPE_CHECKING:

    from .ratl_replicas import Ratl_Replicas

    from .groups import Groups



class Users(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    email: typing.Optional[str] = Field(alias="cq:email", default=None)
    
    encrypted_password: typing.Optional[str] = Field(alias="cq:encrypted_password", default=None)
    
    fullname: typing.Optional[str] = Field(alias="cq:fullname", default=None)
    
    groups: typing.List[typing.Union[Resource, 'Groups']] = Field(alias="cq:groups", default_factory=lambda: [])
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    is_appbuilder: typing.Optional[int] = Field(alias="cq:is_appbuilder", default=None)
    
    is_superuser: typing.Optional[int] = Field(alias="cq:is_superuser", default=None)
    
    is_user_maint: typing.Optional[int] = Field(alias="cq:is_user_maint", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[int] = Field(alias="cq:locked_by", default=None)
    
    login_name: typing.Optional[str] = Field(alias="cq:login_name", default=None)
    
    master_dbid: typing.Optional[int] = Field(alias="cq:master_dbid", default=None)
    
    misc_info: typing.Optional[str] = Field(alias="cq:misc_info", default=None)
    
    phone: typing.Optional[str] = Field(alias="cq:phone", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    ratl_priv_mask: typing.Optional[int] = Field(alias="cq:ratl_priv_mask", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16777228"

    @classmethod
    def shape_name(cls) -> str:
        return "users"

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
    


class UsersProperty(Enum):
    dbid = "cq:dbid"
    dcterms__type = "dcterms:type"
    email = "cq:email"
    encrypted_password = "cq:encrypted_password"
    fullname = "cq:fullname"
    groups = "cq:groups"
    history = "cq:history"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    is_active = "cq:is_active"
    is_appbuilder = "cq:is_appbuilder"
    is_superuser = "cq:is_superuser"
    is_user_maint = "cq:is_user_maint"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    login_name = "cq:login_name"
    master_dbid = "cq:master_dbid"
    misc_info = "cq:misc_info"
    phone = "cq:phone"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    ratl_priv_mask = "cq:ratl_priv_mask"
    rdf__type = "rdf:type"
    title = "dcterms:title"
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


def create_users_changeset(
    dbid: typing.Optional[str] = None,
    email: typing.Optional[str] = None,
    encrypted_password: typing.Optional[str] = None,
    fullname: typing.Optional[str] = None,
    groups: typing.Optional[typing.List[typing.Union[Resource, 'Groups']]] = None,
    history: typing.Optional[Resource] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    is_active: typing.Optional[int] = None,
    is_appbuilder: typing.Optional[int] = None,
    is_superuser: typing.Optional[int] = None,
    is_user_maint: typing.Optional[int] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[int] = None,
    login_name: typing.Optional[str] = None,
    master_dbid: typing.Optional[int] = None,
    misc_info: typing.Optional[str] = None,
    phone: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_priv_mask: typing.Optional[int] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    title: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    
) -> Users:
    '''
    Create a Users changeset.
    '''
    fields = {}
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if email is not None:
        fields["cq:email"] = email 
    if encrypted_password is not None:
        fields["cq:encrypted_password"] = encrypted_password 
    if fullname is not None:
        fields["cq:fullname"] = fullname 
    if groups is not None:
        fields["cq:groups"] = groups 
    if history is not None:
        fields["cq:history"] = history 
    if identifier is not None:
        fields["dcterms:identifier"] = identifier 
    if instanceshape is not None:
        fields["oslc:instanceShape"] = instanceshape 
    if is_active is not None:
        fields["cq:is_active"] = is_active 
    if is_appbuilder is not None:
        fields["cq:is_appbuilder"] = is_appbuilder 
    if is_superuser is not None:
        fields["cq:is_superuser"] = is_superuser 
    if is_user_maint is not None:
        fields["cq:is_user_maint"] = is_user_maint 
    if lock_version is not None:
        fields["cq:lock_version"] = lock_version 
    if locked_by is not None:
        fields["cq:locked_by"] = locked_by 
    if login_name is not None:
        fields["cq:login_name"] = login_name 
    if master_dbid is not None:
        fields["cq:master_dbid"] = master_dbid 
    if misc_info is not None:
        fields["cq:misc_info"] = misc_info 
    if phone is not None:
        fields["cq:phone"] = phone 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if ratl_priv_mask is not None:
        fields["cq:ratl_priv_mask"] = ratl_priv_mask 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if title is not None:
        fields["dcterms:title"] = title 
    if version is not None:
        fields["cq:version"] = version 
    

    fields["dcterms:type"] = "users"
    return Users(**fields)