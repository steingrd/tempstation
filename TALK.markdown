# Create app on heroku.com

# Copy git url and add remote called heroku

# git push heroku master
	- magic follows
	- explain what heroku is, what it does, and so on
	- if install fails, previous version is still running (pre-receive hook fails, did not actually push)
	- environment variables for configuration
		- heroku plugins:install git://github.com/ddollar/heroku-config.git

# heroku logs -t

# heroku addons:add tempodb

# heroku addons:open tempodb

	- https://tempo-db.com/manage/

# heroku addons:add iron_cache

# heroku addons:open iron_cache

	- Caches -> Fermonitor
	- https://hud.iron.io/cache/projects/534cec5e557aa60009000114/caches/fermonitor

# heroku addons:add sentry

# heroku addons:open sentry

	- https://app.getsentry.com/fermonitor/production/

# heroku config

	- explain environment variables
	- needs to be configured in IDE
	- shortcuts in EnvironmentUtils

# custom commands
	
	- heroku run src/main/scripts/createkey.sh

# debugging heroku run bash

	- corrupt jar downloaded - wtf?

