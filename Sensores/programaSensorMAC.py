#!/usr/bin/env python3
"""Demo file showing how to use the miflora library. >>>  python3 SensorMAC.py -v poll MAC sensor """
import argparse
import re
import logging
import sys
import MySQLdb

from btlewrap import available_backends, BluepyBackend, GatttoolBackend, PygattBackend

from miflora import miflora_scanner

from miflora.miflora_poller import (
    MI_BATTERY,
    MI_CONDUCTIVITY,
    MI_LIGHT,
    MI_MOISTURE,
    MI_TEMPERATURE,
    MiFloraPoller,
)


def valid_miflora_mac(mac, pat=re.compile(r"C4:7C:8D:[0-9A-F]{2}:[0-9A-F]{2}:[0-9A-F]{2}")):
    """Check for valid mac adresses."""
    if not pat.match(mac.upper()):
        raise argparse.ArgumentTypeError('The MAC address "{}" seems to be in the wrong format'.format(mac))
    return mac



def poll(args):
    """Poll data from the sensor."""
    backend = _get_backend(args)
    poller = MiFloraPoller(args.mac, backend)

        """Parte de base de datos:"""
    nombre = (poller.name())
    temperatura = (poller.parameter_value(MI_TEMPERATURE))
    humedad = (poller.parameter_value(MI_MOISTURE))
    luz = (poller.parameter_value(MI_LIGHT))
    conductividad = (poller.parameter_value(MI_CONDUCTIVITY))
    bateria = (poller.parameter_value(MI_BATTERY))
   
    nombre = "Anthurium andraeanum"
    db = MySQLdb.connect('ip','user','password','myProyectoAgro',port)
    cursor = db.cursor()
    cursor.execute( """INSERT INTO telemetria (nombre,temperatura,humedad,luz,conductividad,bateria,mac) VALUES (%s,%s,%s,%s,%s,%s,%s) """,(nombre,temperatura,humedad,luz,conductividad,bateria,args.mac))
    db.commit()


def scan(args):
    """Scan for sensors."""
    backend = _get_backend(args)
    print('Scanning for 10 seconds...')
    devices = miflora_scanner.scan(backend, 10)
    print('Found {} devices:'.format(len(devices)))
    for device in devices:
        print('  {}'.format(device))


def _get_backend(args):
    """Extract the backend class from the command line arguments."""
    if args.backend == 'gatttool':
        backend = GatttoolBackend
    elif args.backend == 'bluepy':
        backend = BluepyBackend
    elif args.backend == 'pygatt':
        backend = PygattBackend
    else:
        raise Exception('unknown backend: {}'.format(args.backend))
    return backend

def list_backends(_):
    """List all available backends."""
    backends = [b.__name__ for b in available_backends()]
    print('\n'.join(backends))

def history(args):
    """Read the history from the sensor."""
    backend = _get_backend(args)
    print('Getting history from sensor...')
    poller = MiFloraPoller(args.mac, backend)
    history_list = poller.fetch_history()
    print('History returned {} entries.'.format(len(history_list)))
    for entry in history_list:
        print('History from {}'.format(entry.wall_time))
        print("    Temperature: {}".format(entry.temperature))
        print("    Moisture: {}".format(entry.moisture))
        print("    Light: {}".format(entry.light))
        print("    Conductivity: {}".format(entry.conductivity))

def clear_history(args):
    """Clear the sensor history."""
    backend = _get_backend(args)
    print('Deleting sensor history data...')
    poller = MiFloraPoller(args.mac, backend)
    poller.clear_history()

def main():
    """Main function.
    Mostly parsing the command line arguments.
    """
    parser = argparse.ArgumentParser()
    parser.add_argument('--backend', choices=['gatttool', 'bluepy', 'pygatt'], default='gatttool')
    parser.add_argument('-v', '--verbose', action='store_const', const=True)
    subparsers = parser.add_subparsers(help='sub-command help', )
    parser_poll = subparsers.add_parser('poll', help='poll data from a sensor')
    parser_poll.add_argument('mac', type=valid_miflora_mac)
    parser_poll.set_defaults(func=poll)
    parser_scan = subparsers.add_parser('scan', help='scan for devices')
    parser_scan.set_defaults(func=scan)
    parser_scan = subparsers.add_parser('backends', help='list the available backends')
    parser_scan.set_defaults(func=list_backends)
    parser_history = subparsers.add_parser('history', help='get device history')
    parser_history.add_argument('mac', type=valid_miflora_mac)
    parser_history.set_defaults(func=history)
    parser_history = subparsers.add_parser('clear-history', help='clear device history')
    parser_history.add_argument('mac', type=valid_miflora_mac)
    parser_history.set_defaults(func=clear_history)
    args = parser.parse_args()

    if args.verbose:
        logging.basicConfig(level=logging.DEBUG)

    if not hasattr(args, "func"):
        parser.print_help()
        sys.exit(0)

    args.func(args)

if __name__ == '__main__':
    main()
