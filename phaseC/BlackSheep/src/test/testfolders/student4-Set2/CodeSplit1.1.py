class Host(ni_abc.ContribHost):

    """Implement a webhook for GitHub pull requests."""

    route = 'POST', '/github'

    _useful_actions =  {PullRequestEvent.opened.value,
                        PullRequestEvent.unlabeled.value,
                        PullRequestEvent.synchronize.value}

    def __init__(self, server: ni_abc.ServerHost, client: aiohttp.ClientSession,
                 event: PullRequestEvent,
                 request: JSONDict) -> None:
        """Represent a contribution."""
        self.server = server
        self.event = event
        self.request = request
        self._gh = GitHubAPI(client, "the-knights-who-say-ni",
                             oauth_token=server.contrib_auth_token())