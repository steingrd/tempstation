#!/usr/bin/env python

import datetime
import sys
import time
import threading
import temperusb
import requests

interval = 60 # seconds
base_url = 'http://fermonitor.herokuapp.com'

def get_brew_id():
	return sys.argv[1]

def get_auth_key():
	return sys.argv[2]

def get_auth_secret():
	return sys.argv[3]

def read_temperature():
	return temperusb.TemperHandler().get_devices()[0].get_temperature()

def make_url(brew_id, timestamp, temperature):
	return '%s/brews/%s/temperatures?timestamp=%s&temperature=%s' % \
		(base_url, brew_id, timestamp, temperature)

def make_headers():
	return {
		'X-Fermonitor-Key': get_auth_key(),
		'X-Fermonitor-Secret': get_auth_secret()
	}

def post_temperature(brew_id, timestamp, temperature):
	r = requests.post(make_url(brew_id, timestamp, temperature), headers=make_headers())
	if r.status_code != 200:
		print 'Ooops, service returned', r.status_code

def schedule_task(task):
	t = threading.Timer(interval, task)
	t.start()

def make_timestamp():
	return datetime.datetime.now().isoformat()

def read_and_post():
	post_temperature(get_brew_id(), make_timestamp(), read_temperature())
	schedule_task(read_and_post)

if __name__ == '__main__':
	if len(sys.argv) != 4:
		print 'usage: fermonitorclient.py <brewid> <key> <secret>'
		sys.exit(1)

	read_and_post()