declare var styleTag;


function ResponsiveTable(tableSel, resolution) {
    this.tableSel = tableSel;
    this.resolution = resolution;
    this.viewportWidth;
    this.currentStyle;
    this.isAppended = false;

    this.init = function () {
        this.addResizeEvent();
        this.resizeTable();
    };

    /**
     *  get the table thead
     * @returns NodeList
     */
    this.findTh = function () {
        return document.querySelectorAll(this.tableSel + ' th strong');
    };

    /**
     *  get the table trows
     * @returns NodeList
     */
    this.findTr = function () {
        return document.querySelectorAll(this.tableSel + ' tbody tr');
    };

    /**
     * get Th's labels
     * @returns labels:string
     */
    this.getLabels = function () {

        var table = this.findTh(),
            labels = [];
        table.forEach(function (el) {
            return labels.push(el.innerText);
        });
        return labels;
    };

    /**
     * Add labels to td inner html
     */
    this.addLabels = function () {
        var labels = this.getLabels();
        var trs = this.findTr();

        trs.forEach(function (el) {
            var data = el.querySelectorAll('td');

            data.forEach(function (el, idx) {
                el.innerHTML = '<strong class=\'label-added\'>' + labels[idx] + ':</strong> ' + el.innerText;
            });
        });
    };

    /**
     * remove labels from td inner html
     */
    this.removeLabels = function () {
        document.querySelectorAll('.label-added').forEach(function (el) {
            return el.remove();
        });
    };

    /**
     * @TODD Babel this two hide,show functions !!
     * hide table thead on lower resolution
     */

    this.hideThead = function () {
        document.querySelectorAll(this.tableSel + ' thead').forEach(function (el) {
            el.style.display = 'none';
        });
    };

    /**
     * show table thead on lower resolution
     */
    this.showThead = function () {
        document.querySelectorAll(this.tableSel + ' thead').forEach(function (el) {
            el.style.display = 'table-header-group';
        });
    };

    /**
     * Creating a style tag and append it to head
     * @returns style node element
     */
    this.createStyleTag = function () {
        var head = document.head || document.getElementsByTagName('head')[0],
            style = document.createElement('style');

        style.type = 'text/css';

        head.appendChild(style);
        this.currentStyle = style;
        return style;
    };

    /**
     * Apply styles to page - added as text to this.createStyleTag
     * @param {string} style - style to apply String css format 'h1 { font-size: 20px; }'
     */
    this.applyStyle = function (styles) {

        styleTag = this.createStyleTag();

        if (styleTag.styleSheet) {
            // This is required for IE8 and below.
            styleTag.styleSheet.cssText = styles;
        } else {

            styleTag.appendChild(document.createTextNode(styles));
        }
    };

    /**
     * Styles to apply on responsive table
     */
    this.prepStyle = function () {
        return '\n      .label-added {font-weight: bold; min-width: 26%; display: block; float: left; margin-right:10px;}\n      table {}\n      td { display: block; font-size: .9rem; padding:  }\n      tr { padding: 10px 5px; }\n    ';
    };

    this.resizeTable = function () {

        var w = Math.max(document.documentElement.clientWidth, window.innerWidth || 0);
        var h = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);

        //

        if (w <= this.resolution && !this.isAppended) {

            this.applyStyle(this.prepStyle());
            this.addLabels();
            this.hideThead();
            this.isAppended = true;
        } else if (w >= this.resolution && this.isAppended) {

            this.currentStyle.remove();
            this.removeLabels();
            this.showThead();
            this.isAppended = false;
        }
    };

    /**
     * Add Event to aply changes on pased res
     */
    this.addResizeEvent = function () {
        var _this = this;

        window.addEventListener("resize", function () {
            _this.resizeTable();
        });
    };

    this.init();
}

