//argumentos de entrada: -i ip_rasberry -e email -p password -r region
const ewelink = require('ewelink-api');
const minimist = require("minimist");
const Zeroconf = require('ewelink-api/src/classes/Zeroconf');

const args = minimist(process.argv.slice(2));


var wrip = args.i;
var wremail = args.e;
var wrpassword = args.p;
var wrregion = args.r;

(async () => {
    const connection = new ewelink({
        email: wremail,
        password: wrpassword,
        region: wrregion
        });



        const arpTable = await Zeroconf.saveArpTable({ip: wrip });
        console.log('fichero creado:', arpTable);

    })();
