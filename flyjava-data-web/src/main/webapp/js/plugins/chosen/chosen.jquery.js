/*
 Chosen, a Select Box Enhancer for jQuery and Prototype
 by Patrick Filler for Harvest, http://getharvest.com

 Version 1.1.0
 Full source at https://github.com/harvesthq/chosen
 Copyright (c) 2011 Harvest http://getharvest.com

 MIT License, https://github.com/harvesthq/chosen/blob/master/LICENSE.md
 This file is generated by `grunt build`, do not edit it by hand.
 */
(function(){var G,E,C,A,D,F={}.hasOwnProperty,B=function(I,J){for(var H in J){if(F.call(J,H)){I[H]=J[H]}}function K(){this.constructor=I}K.prototype=J.prototype;I.prototype=new K();I.__super__=J.prototype;return I};A=(function(){function H(){this.options_index=0;this.parsed=[]}H.prototype.add_node=function(I){if(I.nodeName.toUpperCase()==="OPTGROUP"){return this.add_group(I)}else{return this.add_option(I)}};H.prototype.add_group=function(J){var O,K,N,M,L,I;O=this.parsed.length;this.parsed.push({array_index:O,group:true,label:this.escapeExpression(J.label),children:0,disabled:J.disabled});L=J.childNodes;I=[];for(N=0,M=L.length;N<M;N++){K=L[N];I.push(this.add_option(K,O,J.disabled))}return I};H.prototype.add_option=function(I,K,J){if(I.nodeName.toUpperCase()==="OPTION"){if(I.text!==""){if(K!=null){this.parsed[K].children+=1}this.parsed.push({array_index:this.parsed.length,options_index:this.options_index,value:I.value,text:I.text,html:I.innerHTML,selected:I.selected,disabled:J===true?J:I.disabled,group_array_index:K,classes:I.className,style:I.style.cssText})}else{this.parsed.push({array_index:this.parsed.length,options_index:this.options_index,empty:true})}return this.options_index+=1}};H.prototype.escapeExpression=function(K){var J,I;if((K==null)||K===false){return""}if(!/[\&\<\>\"\'\`]/.test(K)){return K}J={"<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#x27;","`":"&#x60;"};I=/&(?!\w+;)|[\<\>\"\'\`]/g;return K.replace(I,function(L){return J[L]||"&amp;"})};return H})();A.select_to_array=function(K){var I,L,H,M,J;L=new A();J=K.childNodes;for(H=0,M=J.length;H<M;H++){I=J[H];L.add_node(I)}return L.parsed};E=(function(){function H(J,I){this.form_field=J;this.options=I!=null?I:{};if(!H.browser_is_supported()){return}this.is_multiple=this.form_field.multiple;this.set_default_text();this.set_default_values();this.setup();this.set_up_html();this.register_observers()}H.prototype.set_default_values=function(){var I=this;this.click_test_action=function(J){return I.test_active_click(J)};this.activate_action=function(J){return I.activate_field(J)};this.active_field=false;this.mouse_on_container=false;this.results_showing=false;this.result_highlighted=null;this.allow_single_deselect=(this.options.allow_single_deselect!=null)&&(this.form_field.options[0]!=null)&&this.form_field.options[0].text===""?this.options.allow_single_deselect:false;this.disable_search_threshold=this.options.disable_search_threshold||0;this.disable_search=this.options.disable_search||false;this.enable_split_word_search=this.options.enable_split_word_search!=null?this.options.enable_split_word_search:true;this.group_search=this.options.group_search!=null?this.options.group_search:true;this.search_contains=this.options.search_contains||false;this.single_backstroke_delete=this.options.single_backstroke_delete!=null?this.options.single_backstroke_delete:true;this.max_selected_options=this.options.max_selected_options||Infinity;this.inherit_select_classes=this.options.inherit_select_classes||false;this.display_selected_options=this.options.display_selected_options!=null?this.options.display_selected_options:true;return this.display_disabled_options=this.options.display_disabled_options!=null?this.options.display_disabled_options:true};H.prototype.set_default_text=function(){if(this.form_field.getAttribute("data-placeholder")){this.default_text=this.form_field.getAttribute("data-placeholder")}else{if(this.is_multiple){this.default_text=this.options.placeholder_text_multiple||this.options.placeholder_text||H.default_multiple_text}else{this.default_text=this.options.placeholder_text_single||this.options.placeholder_text||H.default_single_text}}return this.results_none_found=this.form_field.getAttribute("data-no_results_text")||this.options.no_results_text||H.default_no_result_text};H.prototype.mouse_enter=function(){return this.mouse_on_container=true};H.prototype.mouse_leave=function(){return this.mouse_on_container=false};H.prototype.input_focus=function(J){var I=this;if(this.is_multiple){if(!this.active_field){return setTimeout((function(){return I.container_mousedown()}),50)}}else{if(!this.active_field){return this.activate_field()}}};H.prototype.input_blur=function(J){var I=this;if(!this.mouse_on_container){this.active_field=false;return setTimeout((function(){return I.blur_test()}),100)}};H.prototype.results_option_build=function(L){var M,N,J,I,K;M="";K=this.results_data;for(J=0,I=K.length;J<I;J++){N=K[J];if(N.group){M+=this.result_add_group(N)}else{M+=this.result_add_option(N)}if(L!=null?L.first:void 0){if(N.selected&&this.is_multiple){this.choice_build(N)}else{if(N.selected&&!this.is_multiple){this.single_set_selected_text(N.text)}}}}return M};H.prototype.result_add_option=function(I){var K,J;if(!I.search_match){return""}if(!this.include_option_in_results(I)){return""}K=[];if(!I.disabled&&!(I.selected&&this.is_multiple)){K.push("active-result")}if(I.disabled&&!(I.selected&&this.is_multiple)){K.push("disabled-result")}if(I.selected){K.push("result-selected")}if(I.group_array_index!=null){K.push("group-option")}if(I.classes!==""){K.push(I.classes)}J=document.createElement("li");J.className=K.join(" ");J.style.cssText=I.style;J.setAttribute("data-option-array-index",I.array_index);J.innerHTML=I.search_text;return this.outerHTML(J)};H.prototype.result_add_group=function(I){var J;if(!(I.search_match||I.group_match)){return""}if(!(I.active_options>0)){return""}J=document.createElement("li");J.className="group-result";J.innerHTML=I.search_text;return this.outerHTML(J)};H.prototype.results_update_field=function(){this.set_default_text();if(!this.is_multiple){this.results_reset_cleanup()}this.result_clear_highlight();this.results_build();if(this.results_showing){return this.winnow_results()}};H.prototype.reset_single_select_options=function(){var K,J,M,L,I;L=this.results_data;I=[];for(J=0,M=L.length;J<M;J++){K=L[J];if(K.selected){I.push(K.selected=false)}else{I.push(void 0)}}return I};H.prototype.results_toggle=function(){if(this.results_showing){return this.results_hide()}else{return this.results_show()}};H.prototype.results_search=function(I){if(this.results_showing){return this.winnow_results()}else{return this.results_show()}};H.prototype.winnow_results=function(){var J,S,T,Q,O,R,M,I,P,N,L,K,U;this.no_results_clear();O=0;M=this.get_search_text();J=M.replace(/[-[\]{}()*+?.,\\^$|#\s]/g,"\\$&");Q=this.search_contains?"":"^";T=new RegExp(Q+J,"i");N=new RegExp(J,"i");U=this.results_data;for(L=0,K=U.length;L<K;L++){S=U[L];S.search_match=false;R=null;if(this.include_option_in_results(S)){if(S.group){S.group_match=false;S.active_options=0}if((S.group_array_index!=null)&&this.results_data[S.group_array_index]){R=this.results_data[S.group_array_index];if(R.active_options===0&&R.search_match){O+=1}R.active_options+=1}if(!(S.group&&!this.group_search)){S.search_text=S.group?S.label:S.html;S.search_match=this.search_string_match(S.search_text,T);if(S.search_match&&!S.group){O+=1}if(S.search_match){if(M.length){I=S.search_text.search(N);P=S.search_text.substr(0,I+M.length)+"</em>"+S.search_text.substr(I+M.length);S.search_text=P.substr(0,I)+"<em>"+P.substr(I)}if(R!=null){R.group_match=true}}else{if((S.group_array_index!=null)&&this.results_data[S.group_array_index].search_match){S.search_match=true}}}}}this.result_clear_highlight();if(O<1&&M.length){this.update_results_content("");return this.no_results(M)}else{this.update_results_content(this.results_option_build());return this.winnow_results_set_highlight()}};H.prototype.search_string_match=function(K,J){var L,I,M,N;if(J.test(K)){return true}else{if(this.enable_split_word_search&&(K.indexOf(" ")>=0||K.indexOf("[")===0)){I=K.replace(/\[|\]/g,"").split(" ");if(I.length){for(M=0,N=I.length;M<N;M++){L=I[M];if(J.test(L)){return true}}}}}};H.prototype.choices_count=function(){var I,J,L,K;if(this.selected_option_count!=null){return this.selected_option_count}this.selected_option_count=0;K=this.form_field.options;for(J=0,L=K.length;J<L;J++){I=K[J];if(I.selected){this.selected_option_count+=1}}return this.selected_option_count};H.prototype.choices_click=function(I){I.preventDefault();if(!(this.results_showing||this.is_disabled)){return this.results_show()}};H.prototype.keyup_checker=function(J){var I,K;I=(K=J.which)!=null?K:J.keyCode;this.search_field_scale();switch(I){case 8:if(this.is_multiple&&this.backstroke_length<1&&this.choices_count()>0){return this.keydown_backstroke()}else{if(!this.pending_backstroke){this.result_clear_highlight();return this.results_search()}}break;case 13:J.preventDefault();if(this.results_showing){return this.result_select(J)}break;case 27:if(this.results_showing){this.results_hide()}return true;case 9:case 38:case 40:case 16:case 91:case 17:break;default:return this.results_search()}};H.prototype.clipboard_event_checker=function(J){var I=this;return setTimeout((function(){return I.results_search()}),50)};H.prototype.container_width=function(){if(this.options.width!=null){return this.options.width}else{return""+this.form_field.offsetWidth+"px"}};H.prototype.include_option_in_results=function(I){if(this.is_multiple&&(!this.display_selected_options&&I.selected)){return false}if(!this.display_disabled_options&&I.disabled){return false}if(I.empty){return false}return true};H.prototype.search_results_touchstart=function(I){this.touch_started=true;return this.search_results_mouseover(I)};H.prototype.search_results_touchmove=function(I){this.touch_started=false;return this.search_results_mouseout(I)};H.prototype.search_results_touchend=function(I){if(this.touch_started){return this.search_results_mouseup(I)}};H.prototype.outerHTML=function(J){var I;if(J.outerHTML){return J.outerHTML}I=document.createElement("div");I.appendChild(J);return I.innerHTML};H.browser_is_supported=function(){if(window.navigator.appName==="Microsoft Internet Explorer"){return document.documentMode>=8}if(/iP(od|hone)/i.test(window.navigator.userAgent)){return false}if(/Android/i.test(window.navigator.userAgent)){if(/Mobile/i.test(window.navigator.userAgent)){return false}}return true};H.default_multiple_text="Select Some Options";H.default_single_text="Select an Option";H.default_no_result_text="No results match";return H})();G=jQuery;G.fn.extend({chosen:function(H){if(!E.browser_is_supported()){return this}return this.each(function(J){var I,K;I=G(this);K=I.data("chosen");if(H==="destroy"&&K){K.destroy()}else{if(!K){I.data("chosen",new C(this,H))}}})}});C=(function(I){B(H,I);function H(){D=H.__super__.constructor.apply(this,arguments);return D}H.prototype.setup=function(){this.form_field_jq=G(this.form_field);this.current_selectedIndex=this.form_field.selectedIndex;return this.is_rtl=this.form_field_jq.hasClass("chosen-rtl")};H.prototype.set_up_html=function(){var K,J;K=["chosen-container"];K.push("chosen-container-"+(this.is_multiple?"multi":"single"));if(this.inherit_select_classes&&this.form_field.className){K.push(this.form_field.className)}if(this.is_rtl){K.push("chosen-rtl")}J={"class":K.join(" "),"style":"width: "+(this.container_width())+";","title":this.form_field.title};if(this.form_field.id.length){J.id=this.form_field.id.replace(/[^\w]/g,"_")+"_chosen"}this.container=G("<div />",J);if(this.is_multiple){this.container.html('<ul class="chosen-choices"><li class="search-field"><input type="text" value="'+this.default_text+'" class="default" autocomplete="off" style="width:25px;" /></li></ul><div class="chosen-drop"><ul class="chosen-results"></ul></div>')}else{this.container.html('<a class="chosen-single chosen-default" tabindex="-1"><span>'+this.default_text+'</span><div><b></b></div></a><div class="chosen-drop"><div class="chosen-search"><input type="text" autocomplete="off" /></div><ul class="chosen-results"></ul></div>')}this.form_field_jq.hide().after(this.container);this.dropdown=this.container.find("div.chosen-drop").first();this.search_field=this.container.find("input").first();this.search_results=this.container.find("ul.chosen-results").first();this.search_field_scale();this.search_no_results=this.container.find("li.no-results").first();if(this.is_multiple){this.search_choices=this.container.find("ul.chosen-choices").first();this.search_container=this.container.find("li.search-field").first()}else{this.search_container=this.container.find("div.chosen-search").first();this.selected_item=this.container.find(".chosen-single").first()}this.results_build();this.set_tab_index();this.set_label_behavior();return this.form_field_jq.trigger("chosen:ready",{chosen:this})};H.prototype.register_observers=function(){var J=this;this.container.bind("mousedown.chosen",function(K){J.container_mousedown(K)});this.container.bind("mouseup.chosen",function(K){J.container_mouseup(K)});this.container.bind("mouseenter.chosen",function(K){J.mouse_enter(K)});this.container.bind("mouseleave.chosen",function(K){J.mouse_leave(K)});this.search_results.bind("mouseup.chosen",function(K){J.search_results_mouseup(K)});this.search_results.bind("mouseover.chosen",function(K){J.search_results_mouseover(K)});this.search_results.bind("mouseout.chosen",function(K){J.search_results_mouseout(K)});this.search_results.bind("mousewheel.chosen DOMMouseScroll.chosen",function(K){J.search_results_mousewheel(K)});this.search_results.bind("touchstart.chosen",function(K){J.search_results_touchstart(K)});this.search_results.bind("touchmove.chosen",function(K){J.search_results_touchmove(K)});this.search_results.bind("touchend.chosen",function(K){J.search_results_touchend(K)});this.form_field_jq.bind("chosen:updated.chosen",function(K){J.results_update_field(K)});this.form_field_jq.bind("chosen:activate.chosen",function(K){J.activate_field(K)});this.form_field_jq.bind("chosen:open.chosen",function(K){J.container_mousedown(K)});this.form_field_jq.bind("chosen:close.chosen",function(K){J.input_blur(K)});this.search_field.bind("blur.chosen",function(K){J.input_blur(K)});this.search_field.bind("keyup.chosen",function(K){J.keyup_checker(K)});this.search_field.bind("keydown.chosen",function(K){J.keydown_checker(K)});this.search_field.bind("focus.chosen",function(K){J.input_focus(K)});this.search_field.bind("cut.chosen",function(K){J.clipboard_event_checker(K)});this.search_field.bind("paste.chosen",function(K){J.clipboard_event_checker(K)});if(this.is_multiple){return this.search_choices.bind("click.chosen",function(K){J.choices_click(K)})}else{return this.container.bind("click.chosen",function(K){K.preventDefault()})}};H.prototype.destroy=function(){G(this.container[0].ownerDocument).unbind("click.chosen",this.click_test_action);if(this.search_field[0].tabIndex){this.form_field_jq[0].tabIndex=this.search_field[0].tabIndex}this.container.remove();this.form_field_jq.removeData("chosen");return this.form_field_jq.show()};H.prototype.search_field_disabled=function(){this.is_disabled=this.form_field_jq[0].disabled;if(this.is_disabled){this.container.addClass("chosen-disabled");this.search_field[0].disabled=true;if(!this.is_multiple){this.selected_item.unbind("focus.chosen",this.activate_action)}return this.close_field()}else{this.container.removeClass("chosen-disabled");this.search_field[0].disabled=false;if(!this.is_multiple){return this.selected_item.bind("focus.chosen",this.activate_action)}}};H.prototype.container_mousedown=function(J){if(!this.is_disabled){if(J&&J.type==="mousedown"&&!this.results_showing){J.preventDefault()}if(!((J!=null)&&(G(J.target)).hasClass("search-choice-close"))){if(!this.active_field){if(this.is_multiple){this.search_field.val("")}G(this.container[0].ownerDocument).bind("click.chosen",this.click_test_action);this.results_show()}else{if(!this.is_multiple&&J&&((G(J.target)[0]===this.selected_item[0])||G(J.target).parents("a.chosen-single").length)){J.preventDefault();this.results_toggle()}}return this.activate_field()}}};H.prototype.container_mouseup=function(J){if(J.target.nodeName==="ABBR"&&!this.is_disabled){return this.results_reset(J)}};H.prototype.search_results_mousewheel=function(K){var J;if(K.originalEvent){J=-K.originalEvent.wheelDelta||K.originalEvent.detail}if(J!=null){K.preventDefault();if(K.type==="DOMMouseScroll"){J=J*40}return this.search_results.scrollTop(J+this.search_results.scrollTop())}};H.prototype.blur_test=function(J){if(!this.active_field&&this.container.hasClass("chosen-container-active")){return this.close_field()}};H.prototype.close_field=function(){G(this.container[0].ownerDocument).unbind("click.chosen",this.click_test_action);this.active_field=false;this.results_hide();this.container.removeClass("chosen-container-active");this.clear_backstroke();this.show_search_field_default();return this.search_field_scale()};H.prototype.activate_field=function(){this.container.addClass("chosen-container-active");this.active_field=true;this.search_field.val(this.search_field.val());return this.search_field.focus()};H.prototype.test_active_click=function(J){var K;K=G(J.target).closest(".chosen-container");if(K.length&&this.container[0]===K[0]){return this.active_field=true}else{return this.close_field()}};H.prototype.results_build=function(){this.parsing=true;this.selected_option_count=null;this.results_data=A.select_to_array(this.form_field);if(this.is_multiple){this.search_choices.find("li.search-choice").remove()}else{if(!this.is_multiple){this.single_set_selected_text();if(this.disable_search||this.form_field.options.length<=this.disable_search_threshold){this.search_field[0].readOnly=true;this.container.addClass("chosen-container-single-nosearch")}else{this.search_field[0].readOnly=false;this.container.removeClass("chosen-container-single-nosearch")}}}this.update_results_content(this.results_option_build({first:true}));this.search_field_disabled();this.show_search_field_default();this.search_field_scale();return this.parsing=false};H.prototype.result_do_highlight=function(J){var K,O,N,L,M;if(J.length){this.result_clear_highlight();this.result_highlight=J;this.result_highlight.addClass("highlighted");N=parseInt(this.search_results.css("maxHeight"),10);M=this.search_results.scrollTop();L=N+M;O=this.result_highlight.position().top+this.search_results.scrollTop();K=O+this.result_highlight.outerHeight();if(K>=L){return this.search_results.scrollTop((K-N)>0?K-N:0)}else{if(O<M){return this.search_results.scrollTop(O)}}}};H.prototype.result_clear_highlight=function(){if(this.result_highlight){this.result_highlight.removeClass("highlighted")}return this.result_highlight=null};H.prototype.results_show=function(){if(this.is_multiple&&this.max_selected_options<=this.choices_count()){this.form_field_jq.trigger("chosen:maxselected",{chosen:this});return false}this.container.addClass("chosen-with-drop");this.results_showing=true;this.search_field.focus();this.search_field.val(this.search_field.val());this.winnow_results();return this.form_field_jq.trigger("chosen:showing_dropdown",{chosen:this})};H.prototype.update_results_content=function(J){return this.search_results.html(J)};H.prototype.results_hide=function(){if(this.results_showing){this.result_clear_highlight();this.container.removeClass("chosen-with-drop");this.form_field_jq.trigger("chosen:hiding_dropdown",{chosen:this})}return this.results_showing=false};H.prototype.set_tab_index=function(J){var K;if(this.form_field.tabIndex){K=this.form_field.tabIndex;this.form_field.tabIndex=-1;return this.search_field[0].tabIndex=K}};H.prototype.set_label_behavior=function(){var J=this;this.form_field_label=this.form_field_jq.parents("label");if(!this.form_field_label.length&&this.form_field.id.length){this.form_field_label=G("label[for='"+this.form_field.id+"']")}if(this.form_field_label.length>0){return this.form_field_label.bind("click.chosen",function(K){if(J.is_multiple){return J.container_mousedown(K)}else{return J.activate_field()}})}};H.prototype.show_search_field_default=function(){if(this.is_multiple&&this.choices_count()<1&&!this.active_field){this.search_field.val(this.default_text);return this.search_field.addClass("default")}else{this.search_field.val("");return this.search_field.removeClass("default")}};H.prototype.search_results_mouseup=function(J){var K;K=G(J.target).hasClass("active-result")?G(J.target):G(J.target).parents(".active-result").first();if(K.length){this.result_highlight=K;this.result_select(J);return this.search_field.focus()}};H.prototype.search_results_mouseover=function(J){var K;K=G(J.target).hasClass("active-result")?G(J.target):G(J.target).parents(".active-result").first();if(K){return this.result_do_highlight(K)}};H.prototype.search_results_mouseout=function(J){if(G(J.target).hasClass("active-result"||G(J.target).parents(".active-result").first())){return this.result_clear_highlight()}};H.prototype.choice_build=function(L){var K,M,J=this;K=G("<li />",{"class":"search-choice"}).html("<span>"+L.html+"</span>");if(L.disabled){K.addClass("search-choice-disabled")}else{M=G("<a />",{"class":"search-choice-close","data-option-array-index":L.array_index});M.bind("click.chosen",function(N){return J.choice_destroy_link_click(N)});K.append(M)}return this.search_container.before(K)};H.prototype.choice_destroy_link_click=function(J){J.preventDefault();J.stopPropagation();if(!this.is_disabled){return this.choice_destroy(G(J.target))}};H.prototype.choice_destroy=function(J){if(this.result_deselect(J[0].getAttribute("data-option-array-index"))){this.show_search_field_default();if(this.is_multiple&&this.choices_count()>0&&this.search_field.val().length<1){this.results_hide()}J.parents("li").first().remove();return this.search_field_scale()}};H.prototype.results_reset=function(){this.reset_single_select_options();this.form_field.options[0].selected=true;this.single_set_selected_text();this.show_search_field_default();this.results_reset_cleanup();this.form_field_jq.trigger("change");if(this.active_field){return this.results_hide()}};H.prototype.results_reset_cleanup=function(){this.current_selectedIndex=this.form_field.selectedIndex;return this.selected_item.find("abbr").remove()};H.prototype.result_select=function(J){var L,K;if(this.result_highlight){L=this.result_highlight;this.result_clear_highlight();if(this.is_multiple&&this.max_selected_options<=this.choices_count()){this.form_field_jq.trigger("chosen:maxselected",{chosen:this});return false}if(this.is_multiple){L.removeClass("active-result")}else{this.reset_single_select_options()}K=this.results_data[L[0].getAttribute("data-option-array-index")];K.selected=true;this.form_field.options[K.options_index].selected=true;this.selected_option_count=null;if(this.is_multiple){this.choice_build(K)}else{this.single_set_selected_text(K.text)}if(!((J.metaKey||J.ctrlKey)&&this.is_multiple)){this.results_hide()}this.search_field.val("");if(this.is_multiple||this.form_field.selectedIndex!==this.current_selectedIndex){this.form_field_jq.trigger("change",{"selected":this.form_field.options[K.options_index].value})}this.current_selectedIndex=this.form_field.selectedIndex;return this.search_field_scale()}};H.prototype.single_set_selected_text=function(J){if(J==null){J=this.default_text}if(J===this.default_text){this.selected_item.addClass("chosen-default")}else{this.single_deselect_control_build();this.selected_item.removeClass("chosen-default")}return this.selected_item.find("span").text(J)};H.prototype.result_deselect=function(K){var J;J=this.results_data[K];if(!this.form_field.options[J.options_index].disabled){J.selected=false;this.form_field.options[J.options_index].selected=false;this.selected_option_count=null;this.result_clear_highlight();if(this.results_showing){this.winnow_results()}this.form_field_jq.trigger("change",{deselected:this.form_field.options[J.options_index].value});this.search_field_scale();return true}else{return false}};H.prototype.single_deselect_control_build=function(){if(!this.allow_single_deselect){return}if(!this.selected_item.find("abbr").length){this.selected_item.find("span").first().after('<abbr class="search-choice-close"></abbr>')}return this.selected_item.addClass("chosen-single-with-deselect")};H.prototype.get_search_text=function(){if(this.search_field.val()===this.default_text){return""}else{return G("<div/>").text(G.trim(this.search_field.val())).html()}};H.prototype.winnow_results_set_highlight=function(){var K,J;J=!this.is_multiple?this.search_results.find(".result-selected.active-result"):[];K=J.length?J.first():this.search_results.find(".active-result").first();if(K!=null){return this.result_do_highlight(K)}};H.prototype.no_results=function(J){var K;K=G('<li class="no-results">'+this.results_none_found+' "<span></span>"</li>');K.find("span").first().html(J);this.search_results.append(K);return this.form_field_jq.trigger("chosen:no_results",{chosen:this})};H.prototype.no_results_clear=function(){return this.search_results.find(".no-results").remove()};H.prototype.keydown_arrow=function(){var J;if(this.results_showing&&this.result_highlight){J=this.result_highlight.nextAll("li.active-result").first();if(J){return this.result_do_highlight(J)}}else{return this.results_show()}};H.prototype.keyup_arrow=function(){var J;if(!this.results_showing&&!this.is_multiple){return this.results_show()}else{if(this.result_highlight){J=this.result_highlight.prevAll("li.active-result");if(J.length){return this.result_do_highlight(J.first())}else{if(this.choices_count()>0){this.results_hide()}return this.result_clear_highlight()}}}};H.prototype.keydown_backstroke=function(){var J;if(this.pending_backstroke){this.choice_destroy(this.pending_backstroke.find("a").first());return this.clear_backstroke()}else{J=this.search_container.siblings("li.search-choice").last();if(J.length&&!J.hasClass("search-choice-disabled")){this.pending_backstroke=J;if(this.single_backstroke_delete){return this.keydown_backstroke()}else{return this.pending_backstroke.addClass("search-choice-focus")}}}};H.prototype.clear_backstroke=function(){if(this.pending_backstroke){this.pending_backstroke.removeClass("search-choice-focus")}return this.pending_backstroke=null};H.prototype.keydown_checker=function(K){var J,L;J=(L=K.which)!=null?L:K.keyCode;this.search_field_scale();if(J!==8&&this.pending_backstroke){this.clear_backstroke()}switch(J){case 8:this.backstroke_length=this.search_field.val().length;break;case 9:if(this.results_showing&&!this.is_multiple){this.result_select(K)}this.mouse_on_container=false;break;case 13:K.preventDefault();break;case 38:K.preventDefault();this.keyup_arrow();break;case 40:K.preventDefault();this.keydown_arrow();break}};H.prototype.search_field_scale=function(){var R,Q,N,P,O,J,K,M,L;if(this.is_multiple){N=0;K=0;O="position:absolute; left: -1000px; top: -1000px; display:none;";J=["font-size","font-style","font-weight","font-family","line-height","text-transform","letter-spacing"];for(M=0,L=J.length;M<L;M++){P=J[M];O+=P+":"+this.search_field.css(P)+";"}R=G("<div />",{"style":O});R.text(this.search_field.val());G("body").append(R);K=R.width()+25;R.remove();Q=this.container.outerWidth();if(K>Q-10){K=Q-10}return this.search_field.css({"width":K+"px"})}};return H})(E)}).call(this);