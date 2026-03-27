"""
Example demonstrating the usage of AsyncClient for asynchronous RQ1 operations.

This example shows how to:
1. Create and use an AsyncClient
2. Run queries asynchronously
3. Get records by RQ1 number
4. Handle pagination asynchronously
"""

import asyncio
from rq1 import AsyncClient, BaseUrl
from rq1.models import Issue


async def main():
    """Main async function demonstrating AsyncClient usage."""
    
    # Initialize the AsyncClient
    async with AsyncClient(
        base_url=BaseUrl.ACCEPTANCE,  # or BaseUrl.PRODUCTIVE, BaseUrl.APPROVAL
        username="your_username",
        password="your_password",
        toolname="AsyncExample",
        toolversion="1.0.0"
    ) as client:
        
        print("=== AsyncClient Example ===")
        
        # Example 1: Query issues asynchronously
        print("\n1. Querying issues asynchronously...")
        try:
            query_result = await client.query(
                rtype=Issue,
                where='cq:state="Open"',
                select="*",
                page_size=5
            )
            print(f"Found {query_result.total_count} issues")
            print(f"First page contains {len(query_result.members)} issues")
            
            # Display first few issues
            for i, issue in enumerate(query_result.members[:3]):
                print(f"  Issue {i+1}: {issue.cq__id} - {issue.dcterms__title}")
        
        except Exception as e:
            print(f"Error querying issues: {e}")
        
        # Example 2: Get a specific record by RQ1 number
        print("\n2. Getting record by RQ1 number...")
        try:
            # Replace with an actual RQ1 number from your system
            rq1_number = "ISSUE-12345"  # Example RQ1 number
            issue = await client.get_record_by_rq1_number(Issue, rq1_number)
            print(f"Retrieved issue: {issue.cq__id} - {issue.dcterms__title}")
        
        except ValueError as e:
            print(f"Issue not found: {e}")
        except Exception as e:
            print(f"Error getting issue: {e}")
        
        # Example 3: Pagination with async
        print("\n3. Handling pagination asynchronously...")
        try:
            query_result = await client.query(
                rtype=Issue,
                select=["cq:id", "dcterms:title"],
                page_size=2
            )
            
            page_count = 1
            current_page = query_result
            
            while current_page and page_count <= 3:  # Limit to 3 pages for demo
                print(f"  Page {page_count}: {len(current_page.members)} issues")
                for issue in current_page.members:
                    print(f"    - {issue.cq__id}: {issue.dcterms__title}")
                
                # Get next page asynchronously
                current_page = await client.get_next_query_page(current_page)
                page_count += 1
                
        except Exception as e:
            print(f"Error with pagination: {e}")
        
        # Example 4: Concurrent operations
        print("\n4. Running concurrent queries...")
        try:
            # Run multiple queries concurrently
            tasks = [
                client.query(Issue, where='cq:state="Open"', page_size=1),
                client.query(Issue, where='cq:state="Closed"', page_size=1),
                client.query(Issue, where='cq:state="InProgress"', page_size=1),
            ]
            
            results = await asyncio.gather(*tasks, return_exceptions=True)
            
            states = ["Open", "Closed", "InProgress"]
            for i, result in enumerate(results):
                if isinstance(result, Exception):
                    print(f"  {states[i]} issues: Error - {result}")
                else:
                    print(f"  {states[i]} issues: {result.total_count}")
        
        except Exception as e:
            print(f"Error with concurrent operations: {e}")


async def example_with_custom_connector():
    """Example showing how to use AsyncClient with custom aiohttp connector."""
    import aiohttp
    import ssl
    
    # Create custom SSL context
    ssl_context = ssl.create_default_context()
    ssl_context.check_hostname = False
    ssl_context.verify_mode = ssl.CERT_NONE
    
    # Create custom connector with specific settings
    connector = aiohttp.TCPConnector(
        ssl=ssl_context,
        limit=100,  # Connection pool limit
        limit_per_host=10,  # Connections per host
        keepalive_timeout=30,
        enable_cleanup_closed=True
    )
    
    async with AsyncClient(
        base_url=BaseUrl.ACCEPTANCE,
        username="your_username",
        password="your_password",
        toolname="AsyncExampleCustom",
        toolversion="1.0.0",
        connector=connector
    ) as client:
        
        print("=== AsyncClient with Custom Connector ===")
        
        # Your async operations here
        try:
            query_result = await client.query(Issue, page_size=1)
            print(f"Query successful with custom connector: {query_result.total_count} total issues")
        except Exception as e:
            print(f"Error with custom connector: {e}")


if __name__ == "__main__":
    print("Running AsyncClient examples...")
    print("Note: Update the username, password, and RQ1 numbers before running!")
    
    # Run the main example
    asyncio.run(main())
    
    print("\n" + "="*50)
    
    # Run the custom connector example
    asyncio.run(example_with_custom_connector())
