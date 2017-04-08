package project.bsts.semut.helper;


public class TagsType {

    private final static String[] TITLES = {"Lalu Lintas", "Polisi", "Kecelakaan", "Bencana Alam", "Penutupan Jalan", "Tanda Lainnya", "Angkutan Umum"};

    public static String[] get(int title, int subTitle){

        String[] subTitlesStr = {};
        switch (title){
            case 0:
                subTitlesStr = new String[]{"Normal", "Padat", "Macet Total"};
                break;
            case 1:
                subTitlesStr = new String[]{"Polisi Patroli", "Pemeriksaan Polisi"};
                break;
            case 2:
                subTitlesStr = new String[]{"Kecelakaan", "Kecelakaan dengan Korban Jiwa", "Mobil Mogok"};
                break;
            case 3:
                subTitlesStr = new String[]{"Phon Tumbang", "Banjir"};
                break;
            case 4:
                subTitlesStr = new String[]{"Perbaikan Jalan Ringan", "Perbaikan Jalan Rusak", "Event", "Pembangunan", "Demonstrasi"};
                break;
            case 5:
                subTitlesStr = new String[]{"Tidak Ada Tanda", "Jalan Rusak", "Bus Berhenti", "Tempat Padat/Ramai"};
                break;
            case 6:
                subTitlesStr = new String[]{"BRT", "Angkot"};
                break;
        }
        return new String[]{TITLES[title], subTitlesStr[subTitle]};
    }
}
