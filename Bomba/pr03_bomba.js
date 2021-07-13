const ewelink = require('ewelink-api');
const Zeroconf = require('ewelink-api/src/classes/Zeroconf');
const minimist = require("minimist");

const args = minimist(process.argv.slice(2));

var wrdeviceid = args.d;
var wrestado = args.e;
var wrbomba = args.b;


/* load cache files */
(async () => {


const devicesCache = await Zeroconf.loadCachedDevices();
//console.log(devicesCache);

const arpTable = await Zeroconf.loadArpTable();
//console.log(arpTable);

/* create the connection using cache files */
const connection2 = new ewelink({ devicesCache, arpTable });

/* turn device on/off */
const status = await connection2.setDevicePowerState(devicesCache, wrestado, wrbomba);
console.log(status);


})();
