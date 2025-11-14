# RQ1 Tool Registration Request

## Tool Information

**Tool Name**: `PQA_Agent`  
**Tool Version**: `1.0.0`  
**Purpose**: Developer workflow assistant with RQ1 integration via MCP protocol  
**Technology**: Python, building-block-rq1 library  

## Request Details

### Requesting User
- **Name**: [Your Name]
- **Email**: [Your Email]
- **Department**: [Your Department]

### Tool Description
PQA_Agent is an MCP (Model Context Protocol) server that provides AI assistants (like GitHub Copilot) with access to RQ1/ClearQuest system for developer workflow automation.

### Main Features
1. Query Issues assigned to user
2. Search Issues by title/status
3. Get detailed Issue information
4. View Issue history
5. (Future) PQA compliance checking

### Expected Usage
- **Users**: Developers in your team
- **Frequency**: Multiple queries per day per user
- **Peak Times**: Business hours
- **Estimated Volume**: ~100-500 API calls per day

### Technical Details
- **Protocol**: OSLC API via building-block-rq1
- **Authentication**: Basic Auth with user credentials
- **Environments**: Will use ACCEPTANCE for testing, then PRODUCTIVE

## Why Whitelist is Needed

RQ1 requires all client tools to be registered via the `x-requester` header:
```
x-requester: toolname=PQA_Agent;toolversion=1.0.0;user={username}
```

Without whitelist approval, API requests will be rejected with 403 Forbidden.

## Contact for Approval

**RQ1 Administrators**: [Find RQ1 admin contact in your organization]

Common contacts:
- RQ1 Support Email: [Your org's RQ1 support email]
- Service Desk Ticket: [Create ticket in your org's system]
- RQ1 Wiki/Documentation: [Your org's RQ1 documentation]

## Testing Plan

After approval:
1. Test connection with ACCEPTANCE environment
2. Verify basic query operations
3. Test all MCP tools
4. Monitor for any issues
5. Switch to PRODUCTIVE after validation

## Rollback Plan

If issues occur:
- Can be immediately disabled by removing from whitelist
- No system changes required
- Purely API client, no server-side modifications

---

**Status**: ? Pending Approval  
**Requested Date**: [Today's Date]  
**Approved Date**: [To be filled]  
**Approved By**: [To be filled]
