import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.IssueManager
import com.atlassian.jira.issue.watchers.WatcherManager
import com.atlassian.jira.issue.worklog.WorklogImpl
import com.atlassian.jira.issue.worklog.WorklogManager
import com.atlassian.jira.user.ApplicationUser

IssueManager issueManager = ComponentAccessor.getIssueManager()
WatcherManager watcherManager = ComponentAccessor.getWatcherManager()
WorklogManager worklogManager = ComponentAccessor.getWorklogManager()
ApplicationUser curUser = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser()
List<Issue> allIssue = issueManager.getWatchedIssues(curUser)

for (issue in allIssue) {
    List<ApplicationUser> watchers = issueManager.getWatchersFor(issue)
    for (user in watchers){
        if (!user.equals(curUser)) {
            watcherManager.stopWatching(user, issue);
        }
    }
    WorklogImpl workLog = new WorklogImpl(worklogManager, issue, null, issue.reporter.name, issue.summary, new Date(), null, null, 1*3600)
    worklogManager.create(curUser, workLog, 0L, false)
}
