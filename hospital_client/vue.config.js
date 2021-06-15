const fs = require('fs')

module.exports = {
    devServer: {
    port:3030,
    host: 'localhost',
    https: true,
    key: fs.readFileSync('..\\hospital_platform\\ssl\\keystore\\hospital.key'),
    cert: fs.readFileSync('..\\hospital_platform\\ssl\\keystore\\hospital.crt'),
    hotOnly: false,
    }
}