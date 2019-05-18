//package ajou.com.skechip.Fragment;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import ajou.com.skechip.Adapter.EP_CustomAdapter;
//import ajou.com.skechip.Fragment.bean.Cell;
//import ajou.com.skechip.Fragment.bean.ColTitle;
//import ajou.com.skechip.Fragment.bean.RowTitle;
//import ajou.com.skechip.R;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//import cn.zhouchaoyuan.excelpanel.ExcelPanel;
//
//public class TimeTableFragment extends Fragment {
//
//    public static final String WEEK_FORMAT_PATTERN = "EEEE";
//    public static final String[] PLACE_NAME = {"팔달325","팔달409","성호310","팔달410","팔달309","팔달409"};
//    public static final String[] SUBJECT_NAME = {"인공지능", "정보보호", "이산수학", "확률통계", "캡디","컴파일러"};
//    public static final long ONE_DAY = 24 * 3600 * 1000;
//    public static final int PAGE_SIZE = 5;
//    public static final int ROW_SIZE = 9;
//    private boolean revise_mode;
//    private ExcelPanel excelPanel;
//    private ProgressBar progress;
//    private EP_CustomAdapter adapter;
//    private List<RowTitle> rowTitles;
//    private List<ColTitle> colTitles;
//    private List<List<Cell>> cells;
//    private SimpleDateFormat weekFormatPattern;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.fragment_excel, container, false);
//        Bundle bundle = getArguments();
//        progress =  root.findViewById(R.id.progress);
//        excelPanel =  root.findViewById(R.id.content_container);
//        adapter = new EP_CustomAdapter(getActivity(), blockListener);
//        excelPanel.setAdapter(adapter);
////        revise_mode = bundle.getBoolean("revise_mode");
//
//        initData();
//        return root;
//    }
//
//    private View.OnClickListener blockListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Cell cell = (Cell) view.getTag();
//            if(revise_mode) {   //수정중인 모드
//                if (cell.getSubjectName() == null) {
////                    Toast.makeText(getActivity(), "empty", Toast.LENGTH_SHORT).show();
//
//                }
//                else {
////                    Toast.makeText(getActivity(), cell.getSubjectName(), Toast.LENGTH_SHORT).show();
//
//                }
//            }
//            else{
//                if(cell.getSubjectName()==null) Toast.makeText(getActivity(), "empty", Toast.LENGTH_SHORT).show();
//                else Toast.makeText(getActivity(), cell.getSubjectName(), Toast.LENGTH_SHORT).show();
//            }
//        }
//    };
//
//    private void initData() {
//        weekFormatPattern = new SimpleDateFormat(WEEK_FORMAT_PATTERN);
//        rowTitles = new ArrayList<>();
//        colTitles = new ArrayList<>();
//        cells = new ArrayList<>();
//        for (int i = 0; i < ROW_SIZE; i++) {
//            cells.add(new ArrayList<Cell>());
//        }
//        setOption();
//    }
//
//    private void setOption(){
//        long startTime =  24 * 3600 * 1000 * 4;
//        List<RowTitle> rowTitles1 = genRowData(startTime);
//
//        List<List<Cell>> cells1 = genCellData();
//        rowTitles.addAll(rowTitles1);
//        for (int i = 0; i < cells1.size(); i++) {
//            cells.get(i).addAll(cells1.get(i));
//        }
//        if (colTitles.size() == 0) {
//            colTitles.addAll(genColData());
//        }
//        progress.setVisibility(View.GONE);
//        adapter.setAllData(colTitles, rowTitles, cells);
//        adapter.disableFooter();
//        adapter.disableHeader();
//    }
//
//    private List<RowTitle> genRowData(long startTime) {
//        List<RowTitle> rowTitles = new ArrayList<>();
//        for (int i = 0; i < PAGE_SIZE; i++) {
//            RowTitle rowTitle = new RowTitle();
//            rowTitle.setWeekString(weekFormatPattern.format(startTime + i * ONE_DAY));
//            rowTitles.add(rowTitle);
//        }
//        return rowTitles;
//    }
//
//    private List<ColTitle> genColData() {
//        List<ColTitle> colTitles = new ArrayList<>();
//        int hour = 9;
//        List<String> minute = new ArrayList<String>();
//        minute.add(":00");
//        minute.add(":30");
//
//        for (int i = 1; i < ROW_SIZE; i++) {
//            ColTitle colTitle = new ColTitle();
//            String c = new Character((char)(i+64)).toString();
//            colTitle.setRoomNumber(c);
//            if(i%2==1) {
//                String str= hour+minute.get(0) + "~";
//                hour++;
//                str+=hour+minute.get(1);
//                colTitle.setRoomTypeName(str);
//            }
//            else{
//                String str= hour+minute.get(1) + "~";
//                hour+=2;
//                str+=hour+minute.get(0);
//                colTitle.setRoomTypeName(str);
//            }
//            colTitles.add(colTitle);
//        }
//        return colTitles;
//    }
//
//    //====================================generate data==========================================
//    private ArrayList<List<Cell>> genCellData() {
//        ArrayList<List<Cell>> cells = new ArrayList<List<Cell>>();
//        for(int i=1;i<ROW_SIZE;i++){
//            List<Cell> cellList = new ArrayList<>();
//            cells.add(cellList);
//            for (int j= 0; j < PAGE_SIZE; j++) {
//                Cell cell = new Cell();
//                Random random = new Random();
//                int number = random.nextInt(15);
//                if (number == 1 || number == 2 || number == 3 || number == 4 || number == 5) {
//                    cell.setStatus(number);
//                    cell.setPlaceName(PLACE_NAME[number]);
//                    cell.setSubjectName(SUBJECT_NAME[number]);
////                    Log.e("Cell val:",""+NAME[number]+i+j);
//                } else {
//                    cell.setStatus(0);
////                    Log.e("Cell val:",""+0);
//                }
//                cellList.add(cell);
//            }
//        }
//        return cells;
//    }
//}
