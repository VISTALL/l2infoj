namespace L2InfoJ
{
	partial class MessageWindow
	{
		/// <summary>
		/// Требуется переменная конструктора.
		/// </summary>
		private System.ComponentModel.IContainer components = null;

		/// <summary>
		/// Освободить все используемые ресурсы.
		/// </summary>
		/// <param name="disposing">истинно, если управляемый ресурс должен быть удален; иначе ложно.</param>
		protected override void Dispose(bool disposing)
		{
			if (disposing && (components != null))
			{
				components.Dispose();
			}
			base.Dispose(disposing);
		}

		#region Код, автоматически созданный конструктором форм Windows

		/// <summary>
		/// Обязательный метод для поддержки конструктора - не изменяйте
		/// содержимое данного метода при помощи редактора кода.
		/// </summary>
		private void InitializeComponent()
		{
			System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MessageWindow));
			this._msg = new System.Windows.Forms.Label();
			this._okBtn = new System.Windows.Forms.Button();
			this.SuspendLayout();
			// 
			// _msg
			// 
			this._msg.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
						| System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this._msg.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
			this._msg.Location = new System.Drawing.Point(12, 11);
			this._msg.Name = "_msg";
			this._msg.Size = new System.Drawing.Size(262, 23);
			this._msg.TabIndex = 0;
			this._msg.Text = "Message";
			this._msg.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
			// 
			// _okBtn
			// 
			this._okBtn.Location = new System.Drawing.Point(104, 39);
			this._okBtn.Name = "_okBtn";
			this._okBtn.Size = new System.Drawing.Size(75, 23);
			this._okBtn.TabIndex = 1;
			this._okBtn.Text = "OK";
			this._okBtn.UseVisualStyleBackColor = true;
			this._okBtn.Click += new System.EventHandler(this._okBtn_Click);
			// 
			// MessageWindow
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.ClientSize = new System.Drawing.Size(286, 68);
			this.Controls.Add(this._okBtn);
			this.Controls.Add(this._msg);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedToolWindow;
			this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
			this.Name = "MessageWindow";
			this.Text = "L2InfoJ";
			this.Load += new System.EventHandler(this.Message_Load);
			this.ResumeLayout(false);

		}

		#endregion

		private System.Windows.Forms.Label _msg;
		private System.Windows.Forms.Button _okBtn;
	}
}

