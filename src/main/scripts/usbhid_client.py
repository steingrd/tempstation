#!/usr/bin/env python

import datetime
import sys
import time
import threading
import temperusb
import requests
import pytz
import urllib

interval = 60 # seconds
base_url = 'http://temperaturemonitor.herokuapp.com'

def get_brew_id():
	return sys.argv[1]

def get_auth_key():
	return sys.argv[2]

def get_auth_secret():
	return sys.argv[3]

def read_temperature():
	try:
		t = temperusb.TemperHandler().get_devices()[0].get_temperature()
		return t
	except: 
		print 'Ooops, could not retrieve temperature'
		return None

def make_url(brew_id, timestamp, temperature):
	params = urllib.urlencode({'timestamp': timestamp, 'temperature': temperature})
	return '%s/brews/%s/temperatures?%s' % (base_url, brew_id, params)

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
	return datetime.datetime.utcnow().replace(tzinfo = pytz.utc).strftime("%Y-%m-%dT%H:%M:%S%z")

def read_and_post():
	temperature = read_temperature()
	if temperature is not None:
		post_temperature(get_brew_id(), make_timestamp(), temperature)
	schedule_task(read_and_post)

if __name__ == '__main__':
	if len(sys.argv) != 4:
		print 'usage: usbhid_client.py <brewid> <key> <secret>'
		sys.exit(1)

	read_and_post()
