"use strict";
$(document).ready(function () {
    $(".datepicker").datepicker({defaultDate: +7, showOtherMonths: true, autoSize: true, appendText: '', dateFormat: "yy-mm-dd"});
    $(".inlinepicker").datepicker({inline: true, showOtherMonths: true});
    $(".datepicker-fullscreen").pickadate();
    $(".timepicker-fullscreen").pickatime();
    var a = $("body")[0].style;
    $("#colorpicker-event").colorpicker().on("changeColor", function (b) {
        a.backgroundColor = b.color.toHex()
    });
    $(".btn-notification").click(function () {
        var b = $(this);
        noty({text: b.data("text"), type: b.data("type"), layout: b.data("layout"), timeout: 2000, modal: b.data("modal"), buttons: (b.data("type") != "confirm") ? false : [
            {addClass: "btn btn-primary", text: "Ok", onClick: function (c) {
                c.close();
                noty({force: true, text: 'You clicked "Ok" button', type: "success", layout: b.data("layout")})
            }},
            {addClass: "btn btn-danger", text: "Cancel", onClick: function (c) {
                c.close();
                noty({force: true, text: 'You clicked "Cancel" button', type: "error", layout: b.data("layout")})
            }}
        ]});
        return false
    });
    $(".btn-nprogress-start").click(function () {
        NProgress.start();
        $("#nprogress-info-msg").slideDown(200)
    });
    $(".btn-nprogress-set-40").click(function () {
        NProgress.set(0.4)
    });
    $(".btn-nprogress-inc").click(function () {
        NProgress.inc()
    });
    $(".btn-nprogress-done").click(function () {
        NProgress.done();
        $("#nprogress-info-msg").slideUp(200)
    });
    $("a.basic-alert").click(function (b) {
        b.preventDefault();
        bootbox.alert("Hello world!", function () {
            console.log("Alert Callback")
        })
    });
    $("a.confirm-dialog").click(function (b) {
        b.preventDefault();
        bootbox.confirm("Are you sure?", function (c) {
            console.log("Confirmed: " + c)
        })
    });
    $("a.multiple-buttons").click(function (b) {
        b.preventDefault();
        bootbox.dialog({message: "I am a custom dialog", title: "Custom title", buttons: {success: {label: "Success!", className: "btn-success", callback: function () {
            console.log("great success")
        }}, danger: {label: "Danger!", className: "btn-danger", callback: function () {
            console.log("uh oh, look out!")
        }}, main: {label: "Click ME!", className: "btn-primary", callback: function () {
            console.log("Primary button")
        }}}})
    });
    $("a.multiple-dialogs").click(function (b) {
        b.preventDefault();
        bootbox.alert("Prepare for multiboxes in 1 second...");
        setTimeout(function () {
            bootbox.dialog({message: "Do you like Melon?", title: "Fancy Title", buttons: {danger: {label: "No :-(", className: "btn-danger", callback: function () {
                bootbox.alert("Aww boo. Click the button below to get rid of all these popups.", function () {
                    bootbox.hideAll()
                })
            }}, success: {label: "Oh yeah!", className: "btn-success", callback: function () {
                bootbox.alert("Glad to hear it! Click the button below to get rid of all these popups.", function () {
                    bootbox.hideAll()
                })
            }}}})
        }, 1000)
    });
    $("a.programmatic-close").click(function (c) {
        c.preventDefault();
        var b = bootbox.alert("This dialog will automatically close in two seconds...");
        setTimeout(function () {
            b.modal("hide")
        }, 2000)
    })
});