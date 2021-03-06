/*
 Highcharts Gantt JS v8.2.0 (2020-08-20)

 Tree Grid

 (c) 2016-2019 Jon Arild Nygard

 License: www.highcharts.com/license
*/
(function(a){"object"===typeof module&&module.exports?(a["default"]=a,module.exports=a):"function"===typeof define&&define.amd?define("highcharts/modules/treegrid",["highcharts"],function(A){a(A);a.Highcharts=A;return a}):a("undefined"!==typeof Highcharts?Highcharts:void 0)})(function(a){function A(a,u,v,G){a.hasOwnProperty(u)||(a[u]=G.apply(null,v))}a=a?a._modules:{};A(a,"Gantt/Tree.js",[a["Core/Utilities.js"]],function(a){var u=a.extend,v=a.isNumber,B=a.pick,h=function(a,m){var p=a.reduce(function(e,
p){var a=B(p.parent,"");"undefined"===typeof e[a]&&(e[a]=[]);e[a].push(p);return e},{});Object.keys(p).forEach(function(e,a){var x=p[e];""!==e&&-1===m.indexOf(e)&&(x.forEach(function(e){a[""].push(e)}),delete a[e])});return p},q=function(a,m,p,e,h,x){var y=0,l=0,z=x&&x.after,d=x&&x.before;m={data:e,depth:p-1,id:a,level:p,parent:m};var b,g;"function"===typeof d&&d(m,x);d=(h[a]||[]).map(function(d){var c=q(d.id,a,p+1,d,h,x),f=d.start;d=!0===d.milestone?f:d.end;b=!v(b)||f<b?f:b;g=!v(g)||d>g?d:g;y=y+
1+c.descendants;l=Math.max(c.height+1,l);return c});e&&(e.start=B(e.start,b),e.end=B(e.end,g));u(m,{children:d,descendants:y,height:l});"function"===typeof z&&z(m,x);return m};return{getListOfParents:h,getNode:q,getTree:function(a,m){var p=a.map(function(e){return e.id});a=h(a,p);return q("",null,1,null,a,m)}}});A(a,"Core/Axis/TreeGridTick.js",[a["Core/Utilities.js"]],function(a){var u=a.addEvent,v=a.defined,B=a.isObject,h=a.isNumber,q=a.pick,w=a.wrap,m;(function(a){function e(){this.treeGrid||(this.treeGrid=
new z(this))}function p(d,b){d=d.treeGrid;var g=!d.labelIcon,n=b.renderer,c=b.xy,f=b.options,k=f.width,C=f.height,F=c.x-k/2-f.padding;c=c.y-C/2;var a=b.collapsed?90:180,e=b.show&&h(c),l=d.labelIcon;l||(d.labelIcon=l=n.path(n.symbols[f.type](f.x,f.y,k,C)).addClass("highcharts-label-icon").add(b.group));e||l.attr({y:-9999});n.styledMode||l.attr({"stroke-width":1,fill:q(b.color,"#666666")}).css({cursor:"pointer",stroke:f.lineColor,strokeWidth:f.lineWidth});l[g?"attr":"animate"]({translateX:F,translateY:c,
rotation:a})}function m(d,b,g,n,c,f,k,C,F){var a=q(this.options&&this.options.labels,f);f=this.pos;var e=this.axis,l="treegrid"===e.options.type;d=d.apply(this,[b,g,n,c,a,k,C,F]);l&&(b=a&&B(a.symbol,!0)?a.symbol:{},a=a&&h(a.indentation)?a.indentation:0,f=(f=(e=e.treeGrid.mapOfPosToGridNode)&&e[f])&&f.depth||1,d.x+=b.width+2*b.padding+(f-1)*a);return d}function y(d){var b=this,g=b.pos,a=b.axis,c=b.label,f=a.treeGrid.mapOfPosToGridNode,k=a.options,C=q(b.options&&b.options.labels,k&&k.labels),F=C&&B(C.symbol,
!0)?C.symbol:{},e=(f=f&&f[g])&&f.depth;k="treegrid"===k.type;var l=-1<a.tickPositions.indexOf(g);g=a.chart.styledMode;k&&f&&c&&c.element&&c.addClass("highcharts-treegrid-node-level-"+e);d.apply(b,Array.prototype.slice.call(arguments,1));k&&c&&c.element&&f&&f.descendants&&0<f.descendants&&(a=a.treeGrid.isCollapsed(f),p(b,{color:!g&&c.styles&&c.styles.color||"",collapsed:a,group:c.parentGroup,options:F,renderer:c.renderer,show:l,xy:c.xy}),F="highcharts-treegrid-node-"+(a?"expanded":"collapsed"),c.addClass("highcharts-treegrid-node-"+
(a?"collapsed":"expanded")).removeClass(F),g||c.css({cursor:"pointer"}),[c,b.treeGrid.labelIcon].forEach(function(f){f&&!f.attachedTreeGridEvents&&(u(f.element,"mouseover",function(){c.addClass("highcharts-treegrid-node-active");c.renderer.styledMode||c.css({textDecoration:"underline"})}),u(f.element,"mouseout",function(){var f=v(C.style)?C.style:{};c.removeClass("highcharts-treegrid-node-active");c.renderer.styledMode||c.css({textDecoration:f.textDecoration})}),u(f.element,"click",function(){b.treeGrid.toggleCollapse()}),
f.attachedTreeGridEvents=!0)}))}var l=!1;a.compose=function(d){l||(u(d,"init",e),w(d.prototype,"getLabelPosition",m),w(d.prototype,"renderLabel",y),d.prototype.collapse=function(b){this.treeGrid.collapse(b)},d.prototype.expand=function(b){this.treeGrid.expand(b)},d.prototype.toggleCollapse=function(b){this.treeGrid.toggleCollapse(b)},l=!0)};var z=function(){function d(b){this.tick=b}d.prototype.collapse=function(b){var d=this.tick,a=d.axis,c=a.brokenAxis;c&&a.treeGrid.mapOfPosToGridNode&&(d=a.treeGrid.collapse(a.treeGrid.mapOfPosToGridNode[d.pos]),
c.setBreaks(d,q(b,!0)))};d.prototype.expand=function(b){var d=this.tick,a=d.axis,c=a.brokenAxis;c&&a.treeGrid.mapOfPosToGridNode&&(d=a.treeGrid.expand(a.treeGrid.mapOfPosToGridNode[d.pos]),c.setBreaks(d,q(b,!0)))};d.prototype.toggleCollapse=function(b){var d=this.tick,a=d.axis,c=a.brokenAxis;c&&a.treeGrid.mapOfPosToGridNode&&(d=a.treeGrid.toggleCollapse(a.treeGrid.mapOfPosToGridNode[d.pos]),c.setBreaks(d,q(b,!0)))};return d}();a.Additions=z})(m||(m={}));return m});A(a,"Mixins/TreeSeries.js",[a["Core/Color.js"],
a["Core/Utilities.js"]],function(a,u){var v=u.extend,B=u.isArray,h=u.isNumber,q=u.isObject,w=u.merge,m=u.pick;return{getColor:function(p,e){var h=e.index,q=e.mapOptionsToLevel,y=e.parentColor,l=e.parentColorIndex,z=e.series,d=e.colors,b=e.siblings,g=z.points,n=z.chart.options.chart,c;if(p){g=g[p.i];p=q[p.level]||{};if(q=g&&p.colorByPoint){var f=g.index%(d?d.length:n.colorCount);var k=d&&d[f]}if(!z.chart.styledMode){d=g&&g.options.color;n=p&&p.color;if(c=y)c=(c=p&&p.colorVariation)&&"brightness"===
c.key?a.parse(y).brighten(h/b*c.to).get():y;c=m(d,n,k,c,z.color)}var C=m(g&&g.options.colorIndex,p&&p.colorIndex,f,l,e.colorIndex)}return{color:c,colorIndex:C}},getLevelOptions:function(a){var e=null;if(q(a)){e={};var m=h(a.from)?a.from:1;var p=a.levels;var y={};var l=q(a.defaults)?a.defaults:{};B(p)&&(y=p.reduce(function(a,d){if(q(d)&&h(d.level)){var b=w({},d);var e="boolean"===typeof b.levelIsConstant?b.levelIsConstant:l.levelIsConstant;delete b.levelIsConstant;delete b.level;d=d.level+(e?0:m-1);
q(a[d])?v(a[d],b):a[d]=b}return a},{}));p=h(a.to)?a.to:1;for(a=0;a<=p;a++)e[a]=w({},l,q(y[a])?y[a]:{})}return e},setTreeValues:function x(a,h){var e=h.before,l=h.idRoot,z=h.mapIdToNode[l],d=h.points[a.i],b=d&&d.options||{},g=0,n=[];v(a,{levelDynamic:a.level-(("boolean"===typeof h.levelIsConstant?h.levelIsConstant:1)?0:z.level),name:m(d&&d.name,""),visible:l===a.id||("boolean"===typeof h.visible?h.visible:!1)});"function"===typeof e&&(a=e(a,h));a.children.forEach(function(c,f){var k=v({},h);v(k,{index:f,
siblings:a.children.length,visible:a.visible});c=x(c,k);n.push(c);c.visible&&(g+=c.val)});a.visible=0<g||a.visible;e=m(b.value,g);v(a,{children:n,childrenTotal:g,isLeaf:a.visible&&!g,val:e});return a},updateRootId:function(a){if(q(a)){var e=q(a.options)?a.options:{};e=m(a.rootNode,e.rootId,"");q(a.userOptions)&&(a.userOptions.rootId=e);a.rootNode=e}return e}}});A(a,"Core/Axis/GridAxis.js",[a["Core/Axis/Axis.js"],a["Core/Globals.js"],a["Core/Options.js"],a["Core/Axis/Tick.js"],a["Core/Utilities.js"]],
function(a,u,v,A,h){var q=v.dateFormat,w=h.addEvent,m=h.defined,p=h.erase,e=h.find,B=h.isArray,x=h.isNumber,y=h.merge,l=h.pick,z=h.timeUnits,d=h.wrap;v=u.Chart;var b=function(c){var f=c.options;f.labels||(f.labels={});f.labels.align=l(f.labels.align,"center");c.categories||(f.showLastLabel=!1);c.labelRotation=0;f.labels.rotation=0};"";a.prototype.getMaxLabelDimensions=function(c,f){var a={width:0,height:0};f.forEach(function(f){var k=c[f];f=0;if(h.isObject(k,!0)){var b=h.isObject(k.label,!0)?k.label:
{};k=b.getBBox?b.getBBox().height:0;b.textStr&&(f=Math.round(b.getBBox().width));a.height=Math.max(k,a.height);a.width=Math.max(f,a.width)}});return a};u.dateFormats.W=function(a){a=new this.Date(a);var f=(this.get("Day",a)+6)%7,c=new this.Date(a.valueOf());this.set("Date",c,this.get("Date",a)-f+3);f=new this.Date(this.get("FullYear",c),0,1);4!==this.get("Day",f)&&(this.set("Month",a,0),this.set("Date",a,1+(11-this.get("Day",f))%7));return(1+Math.floor((c.valueOf()-f.valueOf())/6048E5)).toString()};
u.dateFormats.E=function(a){return q("%a",a,!0).charAt(0)};w(v,"afterSetChartSize",function(){this.axes.forEach(function(a){(a.grid&&a.grid.columns||[]).forEach(function(f){f.setAxisSize();f.setAxisTranslation()})})});w(A,"afterGetLabelPosition",function(a){var f=this.label,k=this.axis,c=k.reversed,b=k.chart,d=k.options.grid||{},g=k.options.labels,l=g.align,t=n.Side[k.side],r=a.tickmarkOffset,E=k.tickPositions,D=this.pos-r;E=x(E[a.index+1])?E[a.index+1]-r:k.max+r;var e=k.tickSize("tick");r=e?e[0]:
0;e=e?e[1]/2:0;if(!0===d.enabled){if("top"===t){d=k.top+k.offset;var h=d-r}else"bottom"===t?(h=b.chartHeight-k.bottom+k.offset,d=h+r):(d=k.top+k.len-k.translate(c?E:D),h=k.top+k.len-k.translate(c?D:E));"right"===t?(t=b.chartWidth-k.right+k.offset,c=t+r):"left"===t?(c=k.left+k.offset,t=c-r):(t=Math.round(k.left+k.translate(c?E:D))-e,c=Math.round(k.left+k.translate(c?D:E))-e);this.slotWidth=c-t;a.pos.x="left"===l?t:"right"===l?c:t+(c-t)/2;a.pos.y=h+(d-h)/2;b=b.renderer.fontMetrics(g.style.fontSize,
f.element);f=f.getBBox().height;g.useHTML?a.pos.y+=b.b+-(f/2):(f=Math.round(f/b.h),a.pos.y+=(b.b-(b.h-b.f))/2+-((f-1)*b.h/2));a.pos.x+=k.horiz&&g.x||0}});var g=function(){function a(a){this.axis=a}a.prototype.isOuterAxis=function(){var a=this.axis,b=a.grid.columnIndex,c=a.linkedParent&&a.linkedParent.grid.columns||a.grid.columns,d=b?a.linkedParent:a,g=-1,e=0;a.chart[a.coll].forEach(function(f,b){f.side!==a.side||f.options.isInternal||(e=b,f===d&&(g=b))});return e===g&&(x(b)?c.length===b:!0)};return a}(),
n=function(){function c(){}c.compose=function(f){a.keepProps.push("grid");d(f.prototype,"unsquish",c.wrapUnsquish);w(f,"init",c.onInit);w(f,"afterGetOffset",c.onAfterGetOffset);w(f,"afterGetTitlePosition",c.onAfterGetTitlePosition);w(f,"afterInit",c.onAfterInit);w(f,"afterRender",c.onAfterRender);w(f,"afterSetAxisTranslation",c.onAfterSetAxisTranslation);w(f,"afterSetOptions",c.onAfterSetOptions);w(f,"afterSetOptions",c.onAfterSetOptions2);w(f,"afterSetScale",c.onAfterSetScale);w(f,"afterTickSize",
c.onAfterTickSize);w(f,"trimTicks",c.onTrimTicks);w(f,"destroy",c.onDestroy)};c.onAfterGetOffset=function(){var a=this.grid;(a&&a.columns||[]).forEach(function(a){a.getOffset()})};c.onAfterGetTitlePosition=function(a){if(!0===(this.options.grid||{}).enabled){var f=this.axisTitle,b=this.height,d=this.horiz,g=this.left,e=this.offset,h=this.opposite,t=this.options.title,r=void 0===t?{}:t;t=this.top;var E=this.width,D=this.tickSize(),n=f&&f.getBBox().width,z=r.x||0,m=r.y||0,y=l(r.margin,d?5:10);f=this.chart.renderer.fontMetrics(r.style&&
r.style.fontSize,f).f;D=(d?t+b:g)+(d?1:-1)*(h?-1:1)*(D?D[0]/2:0)+(this.side===c.Side.bottom?f:0);a.titlePosition.x=d?g-n/2-y+z:D+(h?E:0)+e+z;a.titlePosition.y=d?D-(h?b:0)+(h?f:-f)/2+e+m:t-y+m}};c.onAfterInit=function(){var f=this.chart,c=this.options.grid;c=void 0===c?{}:c;var g=this.userOptions;c.enabled&&(b(this),d(this,"labelFormatter",function(a){var f=this.axis,c=this.value,b=f.tickPositions,d=(f.isLinked?f.linkedParent:f).series[0],k=c===b[0];b=c===b[b.length-1];d=d&&e(d.options.data,function(a){return a[f.isXAxis?
"x":"y"]===c});this.isFirst=k;this.isLast=b;this.point=d;return a.call(this)}));if(c.columns)for(var l=this.grid.columns=[],h=this.grid.columnIndex=0;++h<c.columns.length;){var n=y(g,c.columns[c.columns.length-h-1],{linkedTo:0,type:"category",scrollbar:{enabled:!1}});delete n.grid.columns;n=new a(this.chart,n);n.grid.isColumn=!0;n.grid.columnIndex=h;p(f.axes,n);p(f[this.coll],n);l.push(n)}};c.onAfterRender=function(){var a=this.grid,b=this.options,d=this.chart.renderer;if(!0===(b.grid||{}).enabled){this.maxLabelDimensions=
this.getMaxLabelDimensions(this.ticks,this.tickPositions);this.rightWall&&this.rightWall.destroy();if(this.grid&&this.grid.isOuterAxis()&&this.axisLine){var g=b.lineWidth;if(g){var e=this.getLinePath(g),l=e[0],n=e[1],t=((this.tickSize("tick")||[1])[0]-1)*(this.side===c.Side.top||this.side===c.Side.left?-1:1);"M"===l[0]&&"L"===n[0]&&(this.horiz?(l[2]+=t,n[2]+=t):(l[1]+=t,n[1]+=t));this.grid.axisLineExtra?this.grid.axisLineExtra.animate({d:e}):(this.grid.axisLineExtra=d.path(e).attr({zIndex:7}).addClass("highcharts-axis-line").add(this.axisGroup),
d.styledMode||this.grid.axisLineExtra.attr({stroke:b.lineColor,"stroke-width":g}));this.axisLine[this.showAxis?"show":"hide"](!0)}}(a&&a.columns||[]).forEach(function(a){a.render()})}};c.onAfterSetAxisTranslation=function(){var a=this.tickPositions&&this.tickPositions.info,b=this.options,c=b.grid||{},d=this.userOptions.labels||{};this.horiz&&(!0===c.enabled&&this.series.forEach(function(a){a.options.pointRange=0}),a&&b.dateTimeLabelFormats&&b.labels&&!m(d.align)&&(!1===b.dateTimeLabelFormats[a.unitName].range||
1<a.count)&&(b.labels.align="left",m(d.x)||(b.labels.x=3)))};c.onAfterSetOptions=function(a){var b=this.options;a=a.userOptions;var c=b&&h.isObject(b.grid,!0)?b.grid:{};if(!0===c.enabled){var f=y(!0,{className:"highcharts-grid-axis "+(a.className||""),dateTimeLabelFormats:{hour:{list:["%H:%M","%H"]},day:{list:["%A, %e. %B","%a, %e. %b","%E"]},week:{list:["Week %W","W%W"]},month:{list:["%B","%b","%o"]}},grid:{borderWidth:1},labels:{padding:2,style:{fontSize:"13px"}},margin:0,title:{text:null,reserveSpace:!1,
rotation:0},units:[["millisecond",[1,10,100]],["second",[1,10]],["minute",[1,5,15]],["hour",[1,6]],["day",[1]],["week",[1]],["month",[1]],["year",null]]},a);"xAxis"===this.coll&&(m(a.linkedTo)&&!m(a.tickPixelInterval)&&(f.tickPixelInterval=350),m(a.tickPixelInterval)||!m(a.linkedTo)||m(a.tickPositioner)||m(a.tickInterval)||(f.tickPositioner=function(a,b){var c=this.linkedParent&&this.linkedParent.tickPositions&&this.linkedParent.tickPositions.info;if(c){var d,k=f.units;for(d=0;d<k.length;d++)if(k[d][0]===
c.unitName){var g=d;break}if(k[g+1]){var e=k[g+1][0];var l=(k[g+1][1]||[1])[0]}else"year"===c.unitName&&(e="year",l=10*c.count);c=z[e];this.tickInterval=c*l;return this.getTimeTicks({unitRange:c,count:l,unitName:e},a,b,this.options.startOfWeek)}}));y(!0,this.options,f);this.horiz&&(b.minPadding=l(a.minPadding,0),b.maxPadding=l(a.maxPadding,0));x(b.grid.borderWidth)&&(b.tickWidth=b.lineWidth=c.borderWidth)}};c.onAfterSetOptions2=function(a){a=(a=a.userOptions)&&a.grid||{};var b=a.columns;a.enabled&&
b&&y(!0,this.options,b[b.length-1])};c.onAfterSetScale=function(){(this.grid.columns||[]).forEach(function(a){a.setScale()})};c.onAfterTickSize=function(b){var c=a.defaultLeftAxisOptions,f=this.horiz,d=this.maxLabelDimensions,g=this.options.grid;g=void 0===g?{}:g;g.enabled&&d&&(c=2*Math.abs(c.labels.x),f=f?g.cellHeight||c+d.height:c+d.width,B(b.tickSize)?b.tickSize[0]=f:b.tickSize=[f,0])};c.onDestroy=function(a){var b=this.grid;(b.columns||[]).forEach(function(b){b.destroy(a.keepEvents)});b.columns=
void 0};c.onInit=function(a){a=a.userOptions||{};var b=a.grid||{};b.enabled&&m(b.borderColor)&&(a.tickColor=a.lineColor=b.borderColor);this.grid||(this.grid=new g(this))};c.onTrimTicks=function(){var a=this.options,b=this.categories,c=this.tickPositions,d=c[0],g=c[c.length-1],e=this.linkedParent&&this.linkedParent.min||this.min,l=this.linkedParent&&this.linkedParent.max||this.max,t=this.tickInterval;!0!==(a.grid||{}).enabled||b||!this.horiz&&!this.isLinked||(d<e&&d+t>e&&!a.startOnTick&&(c[0]=e),g>
l&&g-t<l&&!a.endOnTick&&(c[c.length-1]=l))};c.wrapUnsquish=function(a){var b=this.options.grid;return!0===(void 0===b?{}:b).enabled&&this.categories?this.tickInterval:a.apply(this,Array.prototype.slice.call(arguments,1))};return c}();(function(a){a=a.Side||(a.Side={});a[a.top=0]="top";a[a.right=1]="right";a[a.bottom=2]="bottom";a[a.left=3]="left"})(n||(n={}));n.compose(a);return n});A(a,"Core/Axis/BrokenAxis.js",[a["Core/Axis/Axis.js"],a["Core/Globals.js"],a["Core/Utilities.js"],a["Extensions/Stacking.js"]],
function(a,u,v,A){var h=v.addEvent,q=v.find,w=v.fireEvent,m=v.isArray,p=v.isNumber,e=v.pick,B=u.Series,x=function(){function h(a){this.hasBreaks=!1;this.axis=a}h.isInBreak=function(a,e){var d=a.repeat||Infinity,b=a.from,g=a.to-a.from;e=e>=b?(e-b)%d:d-(b-e)%d;return a.inclusive?e<=g:e<g&&0!==e};h.lin2Val=function(a){var e=this.brokenAxis;e=e&&e.breakArray;if(!e)return a;var d;for(d=0;d<e.length;d++){var b=e[d];if(b.from>=a)break;else b.to<a?a+=b.len:h.isInBreak(b,a)&&(a+=b.len)}return a};h.val2Lin=
function(a){var e=this.brokenAxis;e=e&&e.breakArray;if(!e)return a;var d=a,b;for(b=0;b<e.length;b++){var g=e[b];if(g.to<=a)d-=g.len;else if(g.from>=a)break;else if(h.isInBreak(g,a)){d-=a-g.from;break}}return d};h.prototype.findBreakAt=function(a,e){return q(e,function(d){return d.from<a&&a<d.to})};h.prototype.isInAnyBreak=function(a,m){var d=this.axis,b=d.options.breaks,g=b&&b.length,n;if(g){for(;g--;)if(h.isInBreak(b[g],a)){var c=!0;n||(n=e(b[g].showPoints,!d.isXAxis))}var f=c&&m?c&&!n:c}return f};
h.prototype.setBreaks=function(l,p){var d=this,b=d.axis,g=m(l)&&!!l.length;b.isDirty=d.hasBreaks!==g;d.hasBreaks=g;b.options.breaks=b.userOptions.breaks=l;b.forceRedraw=!0;b.series.forEach(function(a){a.isDirty=!0});g||b.val2lin!==h.val2Lin||(delete b.val2lin,delete b.lin2val);g&&(b.userOptions.ordinal=!1,b.lin2val=h.lin2Val,b.val2lin=h.val2Lin,b.setExtremes=function(b,c,f,e,g){if(d.hasBreaks){for(var k,h=this.options.breaks;k=d.findBreakAt(b,h);)b=k.to;for(;k=d.findBreakAt(c,h);)c=k.from;c<b&&(c=
b)}a.prototype.setExtremes.call(this,b,c,f,e,g)},b.setAxisTranslation=function(g){a.prototype.setAxisTranslation.call(this,g);d.unitLength=null;if(d.hasBreaks){g=b.options.breaks||[];var c=[],f=[],k=0,l,n=b.userMin||b.min,m=b.userMax||b.max,p=e(b.pointRangePadding,0),q;g.forEach(function(a){l=a.repeat||Infinity;h.isInBreak(a,n)&&(n+=a.to%l-n%l);h.isInBreak(a,m)&&(m-=m%l-a.from%l)});g.forEach(function(a){r=a.from;for(l=a.repeat||Infinity;r-l>n;)r-=l;for(;r<n;)r+=l;for(q=r;q<m;q+=l)c.push({value:q,
move:"in"}),c.push({value:q+(a.to-a.from),move:"out",size:a.breakSize})});c.sort(function(a,b){return a.value===b.value?("in"===a.move?0:1)-("in"===b.move?0:1):a.value-b.value});var t=0;var r=n;c.forEach(function(a){t+="in"===a.move?1:-1;1===t&&"in"===a.move&&(r=a.value);0===t&&(f.push({from:r,to:a.value,len:a.value-r-(a.size||0)}),k+=a.value-r-(a.size||0))});b.breakArray=d.breakArray=f;d.unitLength=m-n-k+p;w(b,"afterBreaks");b.staticScale?b.transA=b.staticScale:d.unitLength&&(b.transA*=(m-b.min+
p)/d.unitLength);p&&(b.minPixelPadding=b.transA*b.minPointOffset);b.min=n;b.max=m}});e(p,!0)&&b.chart.redraw()};return h}();u=function(){function a(){}a.compose=function(a,m){a.keepProps.push("brokenAxis");var d=B.prototype;d.drawBreaks=function(a,d){var b=this,c=b.points,f,g,h,l;if(a&&a.brokenAxis&&a.brokenAxis.hasBreaks){var m=a.brokenAxis;d.forEach(function(d){f=m&&m.breakArray||[];g=a.isXAxis?a.min:e(b.options.threshold,a.min);c.forEach(function(b){l=e(b["stack"+d.toUpperCase()],b[d]);f.forEach(function(c){if(p(g)&&
p(l)){h=!1;if(g<c.from&&l>c.to||g>c.from&&l<c.from)h="pointBreak";else if(g<c.from&&l>c.from&&l<c.to||g>c.from&&l>c.to&&l<c.from)h="pointInBreak";h&&w(a,h,{point:b,brk:c})}})})})}};d.gappedPath=function(){var a=this.currentDataGrouping,d=a&&a.gapSize;a=this.options.gapSize;var e=this.points.slice(),c=e.length-1,f=this.yAxis,k;if(a&&0<c)for("value"!==this.options.gapUnit&&(a*=this.basePointRange),d&&d>a&&d>=this.basePointRange&&(a=d),k=void 0;c--;)k&&!1!==k.visible||(k=e[c+1]),d=e[c],!1!==k.visible&&
!1!==d.visible&&(k.x-d.x>a&&(k=(d.x+k.x)/2,e.splice(c+1,0,{isNull:!0,x:k}),f.stacking&&this.options.stacking&&(k=f.stacking.stacks[this.stackKey][k]=new A(f,f.options.stackLabels,!1,k,this.stack),k.total=0)),k=d);return this.getGraphPath(e)};h(a,"init",function(){this.brokenAxis||(this.brokenAxis=new x(this))});h(a,"afterInit",function(){"undefined"!==typeof this.brokenAxis&&this.brokenAxis.setBreaks(this.options.breaks,!1)});h(a,"afterSetTickPositions",function(){var a=this.brokenAxis;if(a&&a.hasBreaks){var d=
this.tickPositions,e=this.tickPositions.info,c=[],f;for(f=0;f<d.length;f++)a.isInAnyBreak(d[f])||c.push(d[f]);this.tickPositions=c;this.tickPositions.info=e}});h(a,"afterSetOptions",function(){this.brokenAxis&&this.brokenAxis.hasBreaks&&(this.options.ordinal=!1)});h(m,"afterGeneratePoints",function(){var a=this.options.connectNulls,d=this.points,e=this.xAxis,c=this.yAxis;if(this.isDirty)for(var f=d.length;f--;){var k=d[f],h=!(null===k.y&&!1===a)&&(e&&e.brokenAxis&&e.brokenAxis.isInAnyBreak(k.x,!0)||
c&&c.brokenAxis&&c.brokenAxis.isInAnyBreak(k.y,!0));k.visible=h?!1:!1!==k.options.visible}});h(m,"afterRender",function(){this.drawBreaks(this.xAxis,["x"]);this.drawBreaks(this.yAxis,e(this.pointArrayMap,["y"]))})};return a}();u.compose(a,B);return u});A(a,"Core/Axis/TreeGridAxis.js",[a["Core/Axis/Axis.js"],a["Core/Axis/Tick.js"],a["Gantt/Tree.js"],a["Core/Axis/TreeGridTick.js"],a["Mixins/TreeSeries.js"],a["Core/Utilities.js"]],function(a,u,v,A,h,q){var w=h.getLevelOptions,m=q.addEvent,p=q.find,e=
q.fireEvent,B=q.isNumber,x=q.isObject,y=q.isString,l=q.merge,z=q.pick,d=q.wrap,b;(function(a){function b(a,b){var c=a.collapseStart||0;a=a.collapseEnd||0;a>=b&&(c-=.5);return{from:c,to:a,showPoints:!1}}function c(a,b,c){var d=[],f=[],e={},g={},h=-1,k="boolean"===typeof b?b:!1;a=v.getTree(a,{after:function(a){a=g[a.pos];var b=0,c=0;a.children.forEach(function(a){c+=(a.descendants||0)+1;b=Math.max((a.height||0)+1,b)});a.descendants=c;a.height=b;a.collapsed&&f.push(a)},before:function(a){var b=x(a.data,
!0)?a.data:{},c=y(b.name)?b.name:"",f=e[a.parent];f=x(f,!0)?g[f.pos]:null;var r=function(a){return a.name===c},l;k&&x(f,!0)&&(l=p(f.children,r))?(r=l.pos,l.nodes.push(a)):r=h++;g[r]||(g[r]=l={depth:f?f.depth+1:0,name:c,nodes:[a],children:[],pos:r},-1!==r&&d.push(c),x(f,!0)&&f.children.push(l));y(a.id)&&(e[a.id]=a);l&&!0===b.collapsed&&(l.collapsed=!0);a.pos=r}});g=function(a,b){var c=function(a,d,f){var e=d+(-1===d?0:b-1),g=(e-d)/2,h=d+g;a.nodes.forEach(function(a){var b=a.data;x(b,!0)&&(b.y=d+(b.seriesIndex||
0),delete b.seriesIndex);a.pos=h});f[h]=a;a.pos=h;a.tickmarkOffset=g+.5;a.collapseStart=e+.5;a.children.forEach(function(a){c(a,e+1,f);e=(a.collapseEnd||0)-.5});a.collapseEnd=e+.5;return f};return c(a["-1"],-1,{})}(g,c);return{categories:d,mapOfIdToNode:e,mapOfPosToGridNode:g,collapsedNodes:f,tree:a}}function f(a){a.target.axes.filter(function(a){return"treegrid"===a.options.type}).forEach(function(b){var d=b.options||{},f=d.labels,e=d.uniqueNames,g=0,h=d.max;if(!b.treeGrid.mapOfPosToGridNode||b.series.some(function(a){return!a.hasRendered||
a.isDirtyData||a.isDirty})){d=b.series.reduce(function(a,b){b.visible&&((b.options.data||[]).forEach(function(b){x(b,!0)&&(b.seriesIndex=g,a.push(b))}),!0===e&&g++);return a},[]);if(h&&d.length<h)for(var k=d.length;k<=h;k++)d.push({name:k+"\u200b"});d=c(d,e||!1,!0===e?g:1);b.categories=d.categories;b.treeGrid.mapOfPosToGridNode=d.mapOfPosToGridNode;b.hasNames=!0;b.treeGrid.tree=d.tree;b.series.forEach(function(a){var b=(a.options.data||[]).map(function(a){return x(a,!0)?l(a):a});a.visible&&a.setData(b,
!1)});b.treeGrid.mapOptionsToLevel=w({defaults:f,from:1,levels:f&&f.levels,to:b.treeGrid.tree&&b.treeGrid.tree.height});"beforeRender"===a.type&&(b.treeGrid.collapsedNodes=d.collapsedNodes)}})}function g(a,b){var c=this.treeGrid.mapOptionsToLevel||{},d=this.ticks,f=d[b],e;if("treegrid"===this.options.type&&this.treeGrid.mapOfPosToGridNode){var g=this.treeGrid.mapOfPosToGridNode[b];(c=c[g.depth])&&(e={labels:c});f?(f.parameters.category=g.name,f.options=e,f.addLabel()):d[b]=new u(this,b,void 0,void 0,
{category:g.name,tickmarkOffset:g.tickmarkOffset,options:e})}else a.apply(this,Array.prototype.slice.call(arguments,1))}function h(a){var b=this.options;b=(b=b&&b.labels)&&B(b.indentation)?b.indentation:0;var c=a.apply(this,Array.prototype.slice.call(arguments,1));if("treegrid"===this.options.type&&this.treeGrid.mapOfPosToGridNode){var d=this.treeGrid.mapOfPosToGridNode[-1].height||0;c.width+=b*(d-1)}return c}function q(a,b,d){var e=this,g="treegrid"===d.type;e.treeGrid||(e.treeGrid=new I(e));g&&
(m(b,"beforeRender",f),m(b,"beforeRedraw",f),m(b,"addSeries",function(a){a.options.data&&(a=c(a.options.data,d.uniqueNames||!1,1),e.treeGrid.collapsedNodes=(e.treeGrid.collapsedNodes||[]).concat(a.collapsedNodes))}),m(e,"foundExtremes",function(){e.treeGrid.collapsedNodes&&e.treeGrid.collapsedNodes.forEach(function(a){var b=e.treeGrid.collapse(a);e.brokenAxis&&(e.brokenAxis.setBreaks(b,!1),e.treeGrid.collapsedNodes&&(e.treeGrid.collapsedNodes=e.treeGrid.collapsedNodes.filter(function(b){return a.collapseStart!==
b.collapseStart||a.collapseEnd!==b.collapseEnd})))})}),m(e,"afterBreaks",function(){var a;"yAxis"===e.coll&&!e.staticScale&&(null===(a=e.chart.options.chart)||void 0===a?0:a.height)&&(e.isDirty=!0)}),d=l({grid:{enabled:!0},labels:{align:"left",levels:[{level:void 0},{level:1,style:{fontWeight:"bold"}}],symbol:{type:"triangle",x:-5,y:-5,height:10,width:10,padding:5}},uniqueNames:!1},d,{reversed:!0,grid:{columns:void 0}}));a.apply(e,[b,d]);g&&(e.hasNames=!0,e.options.showLastLabel=!0)}function G(a){var b=
this.options;"treegrid"===b.type?(this.min=z(this.userMin,b.min,this.dataMin),this.max=z(this.userMax,b.max,this.dataMax),e(this,"foundExtremes"),this.setAxisTranslation(!0),this.tickmarkOffset=.5,this.tickInterval=1,this.tickPositions=this.treeGrid.mapOfPosToGridNode?this.treeGrid.getTickPositions():[]):a.apply(this,Array.prototype.slice.call(arguments,1))}var H=!1;a.compose=function(a){H||(d(a.prototype,"generateTick",g),d(a.prototype,"getMaxLabelDimensions",h),d(a.prototype,"init",q),d(a.prototype,
"setTickInterval",G),A.compose(u),H=!0)};var I=function(){function a(a){this.axis=a}a.prototype.collapse=function(a){var c=this.axis,d=c.options.breaks||[];a=b(a,c.max);d.push(a);return d};a.prototype.expand=function(a){var c=this.axis,d=c.options.breaks||[],e=b(a,c.max);return d.reduce(function(a,b){b.to===e.to&&b.from===e.from||a.push(b);return a},[])};a.prototype.getTickPositions=function(){var a=this.axis;return Object.keys(a.treeGrid.mapOfPosToGridNode||{}).reduce(function(b,c){c=+c;!(a.min<=
c&&a.max>=c)||a.brokenAxis&&a.brokenAxis.isInAnyBreak(c)||b.push(c);return b},[])};a.prototype.isCollapsed=function(a){var c=this.axis,d=c.options.breaks||[],e=b(a,c.max);return d.some(function(a){return a.from===e.from&&a.to===e.to})};a.prototype.toggleCollapse=function(a){return this.isCollapsed(a)?this.expand(a):this.collapse(a)};return a}();a.Additions=I})(b||(b={}));a.prototype.utils={getNode:v.getNode};b.compose(a);return b});A(a,"masters/modules/treegrid.src.js",[],function(){})});
//# sourceMappingURL=treegrid.js.map