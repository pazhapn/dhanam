(function() {(window.nunjucksPrecompiled = window.nunjucksPrecompiled || {})["test.nunj"] = (function() {
function root(env, context, frame, runtime, cb) {
var lineno = null;
var colno = null;
var output = "";
try {
var parentTemplate = null;
output += "<a href=\"#\" class=\"list-group-item\">\r\n\t<h4 class=\"list-group-item-heading\">Flight Details </h4>\r\n\t<p class=\"list-group-item-text\">FLight Number ";
output += runtime.suppressValue(runtime.contextOrFrameLookup(context, frame, "username"), env.opts.autoescape);
output += "</p>\r\n</a>";
if(parentTemplate) {
parentTemplate.rootRenderFunc(env, context, frame, runtime, cb);
} else {
cb(null, output);
}
;
} catch (e) {
  cb(runtime.handleError(e, lineno, colno));
}
}
return {
root: root
};

})();
})();
