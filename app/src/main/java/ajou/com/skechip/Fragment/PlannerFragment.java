//package ajou.com.skechip.Fragment;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import androidx.annotation.Nullable;
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
//import java.util.Calendar;
//import java.util.List;
//import java.util.Random;
//
//import cn.zhouchaoyuan.excelpanel.ExcelPanel;
//
//public class PlannerFragment extends Fragment implements ExcelPanel.OnLoadMoreListener {
//
//    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
//    public static final String WEEK_FORMAT_PATTERN = "EEEE";
//    public static final String[] CHANNEL = {"팔달325", "팔달409", "성호310", "팔달410", "팔달309", "팔달409"};
//    public static final String[] NAME = {"인공지능", "정보보호", "이산수학", "확률통계", "캡디", "컴파일러"};
//    public static final long ONE_DAY = 24 * 3600 * 1000;
//    public static final int PAGE_SIZE = 8;
//    public static final int ROW_SIZE = 11;
//    public static List<List<Cell>> cells;
//
//    private ExcelPanel excelPanel;
//    private ProgressBar progress;
//    private EP_CustomAdapter adapter;
//    private List<RowTitle> rowTitles;
//    private List<ColTitle> colTitles;
//    private SimpleDateFormat dateFormatPattern;
//    private SimpleDateFormat weekFormatPattern;
//    private boolean isLoading;
//    private long historyStartTime;
//    private long moreStartTime;
//
//    static void get_celldata(List<List<Cell>> data){
//        for (int i = 0; i < data.size(); i++) {
//            cells.get(i).addAll(data.get(i));
//        }
//    }
//
//    public static PlannerFragment newInstance(Bundle bundle) {
//        PlannerFragment fragment = new PlannerFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        if(bundle != null){
//            fragment.setArguments(bundle);
//        }
//        return fragment;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.fragment_excel, container, false);
//        progress = (ProgressBar) root.findViewById(R.id.progress);
//        excelPanel = (ExcelPanel) root.findViewById(R.id.content_container);
//        adapter = new EP_CustomAdapter(getActivity(), blockListener);
//        excelPanel.setAdapter(adapter);
//        excelPanel.setOnLoadMoreListener(this);
//        excelPanel.addOnScrollListener(onScrollListener);
//        initData();
//        return root;
//    }
//
//    private ExcelPanel.OnScrollListener onScrollListener = new ExcelPanel.OnScrollListener() {
//        @Override
//        public void onScrolled(ExcelPanel excelPanel, int dx, int dy) {
//            super.onScrolled(excelPanel, dx, dy);
////            Log.e("acjiji", dx + "     " + dy);
//        }
//    };
//
//    private View.OnClickListener blockListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Cell cell = (Cell) view.getTag();
//            if (cell != null) {
//                Toast.makeText(getActivity(), cell.getSubjectName(), Toast.LENGTH_SHORT).show();
//            }
//        }
//    };
//
//    @Override
//    public void onLoadMore() {
//        if (!isLoading) {
//            loadData(moreStartTime, false);
//        }
//    }
//
//    @Override
//    public void onLoadHistory() {
//        if (!isLoading) {
//            loadData(historyStartTime, true);
//        }
//    }
//
//    private void initData() {
//        moreStartTime = Calendar.getInstance().getTimeInMillis();
//        historyStartTime = moreStartTime - ONE_DAY * PAGE_SIZE;
//        dateFormatPattern = new SimpleDateFormat(DATE_FORMAT_PATTERN);
//        weekFormatPattern = new SimpleDateFormat(WEEK_FORMAT_PATTERN);
//        rowTitles = new ArrayList<>();
//        colTitles = new ArrayList<>();
//        cells = new ArrayList<>();
//        for (int i = 0; i < ROW_SIZE; i++) {
//            cells.add(new ArrayList<Cell>());
//        }
//        loadData(moreStartTime, false);
//    }
//
//    private void loadData(long startTime, final boolean history) {
//        isLoading = true;
//        Message message = new Message();
//        message.arg1 = history ? 1 : 2;
//        message.obj = new Long(startTime);
//        loadDataHandler.sendMessageDelayed(message, 1000);
//    }
//
//    private Handler loadDataHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            isLoading = false;
//            long startTime = (Long) msg.obj;
//            List<RowTitle> rowTitles1 = genRowData(startTime);
//            List<List<Cell>> cells1 = genCellData();
//            if (msg.arg1 == 1) {//history
//                historyStartTime -= ONE_DAY * PAGE_SIZE;
//                rowTitles.addAll(0, rowTitles1);
//                for (int i = 0; i < cells1.size(); i++) {
//                    cells.get(i).addAll(0, cells1.get(i));
//                }
//
//                if (excelPanel != null) {
//                    excelPanel.addHistorySize(PAGE_SIZE);
//                }
//            } else {
//                moreStartTime += ONE_DAY * PAGE_SIZE;
//                rowTitles.addAll(rowTitles1);
//                for (int i = 0; i < cells1.size(); i++) {
//                    cells.get(i).addAll(cells1.get(i));
//                }
//            }
//            if (colTitles.size() == 0) {
//                colTitles.addAll(genColData());
//            }
//            progress.setVisibility(View.GONE);
//            adapter.setAllData(colTitles, rowTitles, cells);
//            adapter.enableFooter();
//            adapter.enableHeader();
//        }
//    };
//    //====================================make data==========================================
//    private List<RowTitle> genRowData(long startTime) {
//        List<RowTitle> rowTitles = new ArrayList<>();
//        Random random = new Random();
//        for (int i = 0; i < PAGE_SIZE; i++) {
//            RowTitle rowTitle = new RowTitle();
//            rowTitle.setDateString(dateFormatPattern.format(startTime + i * ONE_DAY));
//            rowTitle.setWeekString(weekFormatPattern.format(startTime + i * ONE_DAY));
//            rowTitles.add(rowTitle);
//        }
//        return rowTitles;
//    }
//
//    private List<ColTitle> genColData() {
//        List<ColTitle> colTitles = new ArrayList<>();
//        ColTitle tmp = new ColTitle();
//        int hour = 9;
//        List<String> minute = new ArrayList<String>();
//        minute.add(":00");
//        minute.add(":30");
//        tmp.setRoomNumber("Z");
//        tmp.setRoomTypeName("7:30~9:00");
//        colTitles.add(tmp);
//        for (int i = 0; i < ROW_SIZE; i++) {
//            ColTitle colTitle = new ColTitle();
//            String c = new Character((char)(i+65)).toString();
//            colTitle.setRoomNumber(c);
//            if(i%2==0) {
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
//    private List<List<Cell>> genCellData() {
//        List<List<Cell>> cells = new ArrayList<>();
//        for (int i = 1; i < 6; i++) {
//            List<Cell> cellList = new ArrayList<>();
//            cells.add(cellList);
//            for (int j = 1; j < PAGE_SIZE; j++) {
//                Cell cell = new Cell();
//                Random random = new Random();
//                int number = random.nextInt(15);
//                if (number == 1 || number == 2 || number == 3 || number == 4 || number == 5) {
//                    cell.setStatus(number);
//                    cell.setPlaceName(CHANNEL[number]);
//                    cell.setSubjectName(NAME[number]);
//                } else {
//                    cell.setStatus(0);
//                }
//                cellList.add(cell);
//            }
//        }
//        return cells;
//    }
//}