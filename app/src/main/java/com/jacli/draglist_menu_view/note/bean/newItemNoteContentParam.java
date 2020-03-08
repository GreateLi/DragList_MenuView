package com.jacli.draglist_menu_view.note.bean;


import java.util.List;


public class newItemNoteContentParam extends ParamsBean{
    private RemindBean remind;
    private List<NotesBean> notes  ;

    public RemindBean getRemind() {
        return remind;
    }

    public void setRemind(RemindBean remind) {
        this.remind = remind;
    }

    public List<NotesBean> getNotes() {
        return notes;
    }

    public void setNotes(List<NotesBean> notes) {
        this.notes = notes;
    }

    public static class RemindBean {
        /**
         * interval : 0
         * cueTone : 0
         * frequency : 0
         */

        private int intervals;
        public int getIntervals() {
			return intervals;
		}

		public void setIntervals(int intervals) {
			this.intervals = intervals;
		}

		public int getCue_tone() {
			return cue_tone;
		}

		public void setCue_tone(int cue_tone) {
			this.cue_tone = cue_tone;
		}

		private int cue_tone;
        private int frequency;

    
 
        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }
    }

    public static class NotesBean {
        /**
         * notesType : 0
         * notesId : 91
         * notesContent : 559889009hjjj
         * createTime : 1501655406000
         * updateTime : 1501683948000
         * notesStatus : 0
         * userId : 228
         * remind : 0
         */

    	private String id;
     

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		private String notesType;
        private String notesId;
        private String notesContent;
        private long createTime;
        private long updateTime;
        private String notesStatus;
        private int userId;
        private String remind;
        private String circulation;
        public String getCirculation() {
			return circulation;
		}

		public void setCirculation(String circulation) {
			this.circulation = circulation;
		}

		public String getRemindTime() {
			return remindTime;
		}

		public void setRemindTime(String remindTime) {
			this.remindTime = remindTime;
		}

		private String remindTime;
        private String notesFile;
        public int getOrderIndex() {
			return orderIndex;
		}

		public void setOrderIndex(int orderIndex) {
			this.orderIndex = orderIndex;
		}

		private int orderIndex;
        
        public String getNotesFile() {
            return notesFile;
        }

        public void setNotesFile(String notesFile) {
            this.notesFile = notesFile;
        }

        public String getNotesType() {
            return notesType;
        }

        public void setNotesType(String notesType) {
            this.notesType = notesType;
        }

        public String getNotesId() {
            return notesId;
        }

        public void setNotesId(String notesId) {
            this.notesId = notesId;
        }

        public String getNotesContent() {
            return notesContent;
        }

        public void setNotesContent(String notesContent) {
            this.notesContent = notesContent;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public String getNotesStatus() {
            return notesStatus;
        }

        public void setNotesStatus(String notesStatus) {
            this.notesStatus = notesStatus;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getRemind() {
            return remind;
        }

        public void setRemind(String remind) {
            this.remind = remind;
        }
    }

}
