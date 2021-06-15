const fs = require('fs')

module.exports = {
	    devServer: {
		        port:3000,
		        host: 'localhost',
		        https: true,
		        key: fs.readFileSync('..\\admin_platform\\pki\\keystore\\root.key'),
		        cert: fs.readFileSync('..\\admin_platform\\pki\\keystore\\root.crt'),
		        https: true,
		        hotOnly: false,
	    }
}
