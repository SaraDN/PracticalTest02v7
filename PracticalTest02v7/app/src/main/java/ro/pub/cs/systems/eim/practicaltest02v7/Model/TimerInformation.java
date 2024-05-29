package ro.pub.cs.systems.eim.practicaltest02v7.Model;

public class TimerInformation {
        // Atribute
        private String hour;
        private String minute;

        // Constructor
        public TimerInformation(String hour, String minute) {
            this.hour = hour;
            this.minute = minute;
        }

        public TimerInformation() {
            this.hour = null;
            this.minute = null;
        }

        public String getHour() {
            return hour;
        }

        public String getMinute() {
            return minute;
        }
}
