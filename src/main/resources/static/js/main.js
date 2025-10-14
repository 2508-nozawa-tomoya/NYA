function toggleAll(masterCheckbox) {
    // グループ識別子を取得
    const groupId = masterCheckbox.getAttribute('data-group');

    // グループに属するすべてのチェックボックスを取得
    const checkboxes = document.querySelectorAll('.' + groupId.replace('group__', 'checkbox__'));

    // マスターの状態に応じてON/OFF
    checkboxes.forEach(cb => cb.checked = masterCheckbox.checked);
}

function CheckStop(){
	if(confirm('ユーザーを停止しますか？')){
		return true;
	} else{
		return false;
	}
}

function CheckValid(){
	if(confirm('ユーザーを有効にしますか？')){
		return true;
	} else{
		return false;
	}
}

// 勤怠編集画面用
function CheckDelete(){
	if(confirm('勤怠記録を削除しますか？')){
		return true;
	} else{
		return false;
	}
}

function CheckAlter(){
	if(confirm('パスワードを変更しますか?')){
		return true;
	} else{
		return false;
	}
}

// パスワード設定画面用
function togglePasswordVisibility() {
    const password = document.getElementById('passwordInput');
    const confirmation = document.getElementById('confirmationPasswordInput');
    const isChecked = document.getElementById('showPasswordCheckbox').checked;

    password.type = isChecked ? 'text' : 'password';
    confirmation.type = isChecked ? 'text' : 'password';
}

// 承認画面用

//ログイン画面用
document.addEventListener("DOMContentLoaded", function () {
    const toggle = document.getElementById("togglePassword");
    const passwordInput = document.getElementById("password");
    const confirmationPasswordInput = document.getElementById("confirmationPassword");

    toggle.addEventListener("change", function () {
        if (toggle.checked) {
            passwordInput.type = "text";
            confirmationPasswordInput.type = "text";
        } else {
            passwordInput.type = "password";
            confirmationPasswordInput.type = "password";
        }
    });
});

