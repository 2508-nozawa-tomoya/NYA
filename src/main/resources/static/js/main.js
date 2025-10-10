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

// 承認画面用
document.addEventListener("DOMContentLoaded", function () {
    const approveBtn = document.querySelector('button[type="submit"][value="approve"]');
    const rejectBtn = document.querySelector('button[type="submit"][value="reject"]');

    if (approveBtn) {
        approveBtn.addEventListener("click", function (e) {
            const confirmed = confirm("選択された勤怠を承認してもよろしいですか？");
            if (!confirmed) {
                e.preventDefault();
            }
        });
    }

    if (rejectBtn) {
        rejectBtn.addEventListener("click", function (e) {
            const confirmed = confirm("選択された勤怠を差戻してもよろしいですか？");
            if (!confirmed) {
                e.preventDefault();
            }
        });
    }
});