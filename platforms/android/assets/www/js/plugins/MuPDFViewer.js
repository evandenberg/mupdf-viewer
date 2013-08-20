/*
 * cordova is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) 2013, Creative Skills (http://creativeskills.nl)
 */

 cordova.define("cordova/plugins/mupdfviewer", 
    function(require, exports, module) {
        var exec = require('cordova/exec');

        var MuPDFViewer = function() {};

        MuPDFViewer.CLOSE_EVENT = 0;
        MuPDFViewer.LOCATION_CHANGED_EVENT = 1;

        /**
         * Close the browser opened by showWebPage.
         */
        MuPDFViewer.prototype.close = function() {
            cordova.exec(null, null, "MuPDFViewer", "close", []);
        };

        /**
         * Display a MuPDFViewer with the specified URL.
         * This method starts a new web browser activity.
         *
         * @param path           The path to load
         */
        MuPDFViewer.prototype.openPDF = function(path) {
            cordova.exec(null, null, "MuPDFViewer", "openPDF", [ { fileName: path } ]);
        };

        /**
         * Method called when the MuPDFViewer has an event.
         */
        MuPDFViewer.prototype._onEvent = function(data) {
            if (data.type == MuPDFViewer.CLOSE_EVENT && typeof window.plugins.MuPDFViewer.onClose === "function") {
                window.plugins.childBrowser.onClose();
            }
            if (data.type == MuPDFViewer.LOCATION_CHANGED_EVENT && typeof window.plugins.MuPDFViewer.onLocationChange === "function") {
                window.plugins.MuPDFViewer.onLocationChange( data.location );
            }
        };

        /**
         * Method called when the MuPDFViewer has an error.
         */
        MuPDFViewer.prototype._onError = function(data) {
            if (typeof window.plugins.MuPDFViewer.onError === "function") {
                window.plugins.MuPDFViewer.onError( data );
            }
        };

        /**
         * Maintain API consistency with iOS
         */
        MuPDFViewer.prototype.install = function(){
        };

        var mupdfviewer = new MuPDFViewer();
        module.exports = mupdfviewer;
    }
);

/**
 * Load MuPDFViewer
 */
if(!window.plugins) {
    window.plugins = {};
}
if (!window.plugins.MuPDFViewer) {
    window.plugins.MuPDFViewer = cordova.require("cordova/plugins/mupdfviewer");
}