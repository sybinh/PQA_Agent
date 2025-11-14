from .email_rule import Email_Rule, Email_RuleProperty, create_email_rule_changeset
from .ratl_replicas import Ratl_Replicas, Ratl_ReplicasProperty, create_ratl_replicas_changeset
from .release import Release, ReleaseProperty, create_release_changeset
from .rq1_rulesconfig import Rq1_Rulesconfig, Rq1_RulesconfigProperty, create_rq1_rulesconfig_changeset
from .attachmentmapping import Attachmentmapping, AttachmentmappingProperty, create_attachmentmapping_changeset
from .rq1_instructiontype import Rq1_Instructiontype, Rq1_InstructiontypeProperty, create_rq1_instructiontype_changeset
from .groups import Groups, GroupsProperty, create_groups_changeset
from .rq1_roleuser import Rq1_Roleuser, Rq1_RoleuserProperty, create_rq1_roleuser_changeset
from .exchangeprotocol import Exchangeprotocol, ExchangeprotocolProperty, create_exchangeprotocol_changeset
from .commercial import Commercial, CommercialProperty, create_commercial_changeset
from .history import History, HistoryProperty, create_history_changeset
from .issue import Issue, IssueProperty, create_issue_changeset
from .rq1_configuration import Rq1_Configuration, Rq1_ConfigurationProperty, create_rq1_configuration_changeset
from .rq1_tldselector import Rq1_Tldselector, Rq1_TldselectorProperty, create_rq1_tldselector_changeset
from .rq1_webtoolconfig import Rq1_Webtoolconfig, Rq1_WebtoolconfigProperty, create_rq1_webtoolconfig_changeset
from .rq1_logging import Rq1_Logging, Rq1_LoggingProperty, create_rq1_logging_changeset
from .workitem import Workitem, WorkitemProperty, create_workitem_changeset
from .rq1_metadata import Rq1_Metadata, Rq1_MetadataProperty, create_rq1_metadata_changeset
from .background import Background, BackgroundProperty, create_background_changeset
from .issuereleasemap import Issuereleasemap, IssuereleasemapProperty, create_issuereleasemap_changeset
from .commercialacl import Commercialacl, CommercialaclProperty, create_commercialacl_changeset
from .historylog import Historylog, HistorylogProperty, create_historylog_changeset
from .externallink import Externallink, ExternallinkProperty, create_externallink_changeset
from .attachments import Attachments, AttachmentsProperty, create_attachments_changeset
from .project import Project, ProjectProperty, create_project_changeset
from .rq1_rolespermission import Rq1_Rolespermission, Rq1_RolespermissionProperty, create_rq1_rolespermission_changeset
from .problem import Problem, ProblemProperty, create_problem_changeset
from .exchangeconfig import Exchangeconfig, ExchangeconfigProperty, create_exchangeconfig_changeset
from .users import Users, UsersProperty, create_users_changeset
from .releasereleasemap import Releasereleasemap, ReleasereleasemapProperty, create_releasereleasemap_changeset
from .contact import Contact, ContactProperty, create_contact_changeset
from .mapping import Mapping, MappingProperty, create_mapping_changeset
from .issueissuemap import Issueissuemap, IssueissuemapProperty, create_issueissuemap_changeset

Email_Rule.model_rebuild()
Ratl_Replicas.model_rebuild()
Release.model_rebuild()
Rq1_Rulesconfig.model_rebuild()
Attachmentmapping.model_rebuild()
Rq1_Instructiontype.model_rebuild()
Groups.model_rebuild()
Rq1_Roleuser.model_rebuild()
Exchangeprotocol.model_rebuild()
Commercial.model_rebuild()
History.model_rebuild()
Issue.model_rebuild()
Rq1_Configuration.model_rebuild()
Rq1_Tldselector.model_rebuild()
Rq1_Webtoolconfig.model_rebuild()
Rq1_Logging.model_rebuild()
Workitem.model_rebuild()
Rq1_Metadata.model_rebuild()
Background.model_rebuild()
Issuereleasemap.model_rebuild()
Commercialacl.model_rebuild()
Historylog.model_rebuild()
Externallink.model_rebuild()
Attachments.model_rebuild()
Project.model_rebuild()
Rq1_Rolespermission.model_rebuild()
Problem.model_rebuild()
Exchangeconfig.model_rebuild()
Users.model_rebuild()
Releasereleasemap.model_rebuild()
Contact.model_rebuild()
Mapping.model_rebuild()
Issueissuemap.model_rebuild()
