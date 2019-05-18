package ajou.com.skechip.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import ajou.com.skechip.Adapter.EP_CustomAdapter;
import ajou.com.skechip.CalendarActivity;
import ajou.com.skechip.Fragment.bean.Cell;
import ajou.com.skechip.Fragment.bean.ColTitle;
import ajou.com.skechip.Fragment.bean.RowTitle;
import ajou.com.skechip.MeetingCreateActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import ajou.com.skechip.R;
import cn.zhouchaoyuan.excelpanel.ExcelPanel;

import static ajou.com.skechip.Fragment.EP_Fragment.ONE_DAY;
import static ajou.com.skechip.Fragment.EP_Fragment.PAGE_SIZE;
import static ajou.com.skechip.Fragment.EP_Fragment.ROW_SIZE;
import static ajou.com.skechip.Fragment.EP_Fragment.WEEK_FORMAT_PATTERN;


public class SelectMeetingTimeFragment extends Fragment {

    private ExcelPanel excelPanel;
    private List<RowTitle> rowTitles;
    private List<ColTitle> colTitles;
    private List<List<Cell>> cells;
    private SimpleDateFormat weekFormatPattern;
    private EP_CustomAdapter timeTableAdapter;
    private List<Cell> SELECTED_CELLS = new ArrayList<Cell>();

    public List<String> PLACE_NAME = new ArrayList<String>();
    public List<String> SUBJECT_NAME = new ArrayList<String>();


    public static SelectMeetingTimeFragment newInstance(Bundle bundle) {
        SelectMeetingTimeFragment fragment = new SelectMeetingTimeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_meeting_time, container, false);


        //임시
        PLACE_NAME.add("");
        PLACE_NAME.add("팔달325");
        PLACE_NAME.add("팔달409");
        PLACE_NAME.add("팔달410");
        PLACE_NAME.add("팔달309");
        PLACE_NAME.add("팔달409");

        SUBJECT_NAME.add("");
        SUBJECT_NAME.add("정보보호");
        SUBJECT_NAME.add("이산수학");
        SUBJECT_NAME.add("확률통계");
        SUBJECT_NAME.add("캡디");
        SUBJECT_NAME.add("컴파일러");



        excelPanel = view.findViewById(R.id.content_container);
        timeTableAdapter = new EP_CustomAdapter(getActivity(), blockListener);
        excelPanel.setAdapter(timeTableAdapter);

        initData();

        Button confirmTimeBtn = view.findViewById(R.id.confirm_time_button);
        confirmTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : 선택한거 체크하기


                if(SELECTED_CELLS.size() == 0) {
                    Toast.makeText(getActivity(), "선택한 시간이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
//                    Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();


                    ((MeetingCreateActivity) Objects.requireNonNull(getActivity())).onSelectTimesFinishedEvent(SELECTED_CELLS);

                }



            }
        });


        Toast.makeText(getActivity(), "일정을 생성할 시간을 선택하세요", Toast.LENGTH_LONG).show();
        return view;
    }



    private View.OnClickListener blockListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Cell cell = (Cell) view.getTag();
            if (cell.getStatus() == 0) {      //빈칸 클릭
                SELECTED_CELLS.add(cell);
                cell.setStatus(-1);
                timeTableAdapter.setAllData(colTitles, rowTitles, cells);
            } else if (cell.getStatus() == -1) {
                SELECTED_CELLS.remove(cell);
                cell.setStatus(0);
                timeTableAdapter.setAllData(colTitles, rowTitles, cells);
            }
        }
    };


    private void initData() {
        weekFormatPattern = new SimpleDateFormat(WEEK_FORMAT_PATTERN);
        rowTitles = new ArrayList<>();
        colTitles = new ArrayList<>();
        cells = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE; i++) {
            cells.add(new ArrayList<Cell>());
        }
        long startTime = 24 * 3600 * 1000 * 4;
        rowTitles.addAll(genRowData(startTime));
        colTitles.addAll(genColData());

        List<Cell> cells1 = genCellData();

        for (int i = 0; i < ROW_SIZE; i++) {
            List<Cell> tmplist = new ArrayList<Cell>();
            for (int j = 0; j < PAGE_SIZE; j++) {
                Cell tmp = cells1.get(i * PAGE_SIZE + j);
                tmp.setStartTime(colTitles.get(i).getTimeRangeName().split("~")[0]);
                tmp.setWeekofday(rowTitles.get(j).getWeekString());
                tmplist.add(tmp);
            }
            cells.get(i).addAll(tmplist);
        }
        timeTableAdapter.setAllData(colTitles, rowTitles, cells);
        timeTableAdapter.disableFooter();
        timeTableAdapter.disableHeader();
    }

    private List<RowTitle> genRowData(long startTime) {
        List<RowTitle> rowTitles = new ArrayList<>();
        for (int i = 0; i < PAGE_SIZE; i++) {
            RowTitle rowTitle = new RowTitle();
            rowTitle.setWeekString(weekFormatPattern.format(startTime + i * ONE_DAY));
            rowTitles.add(rowTitle);
        }
        return rowTitles;
    }

    private List<ColTitle> genColData() {
        List<ColTitle> colTitles = new ArrayList<>();
        int hour = 9;
        List<String> minute = new ArrayList<String>();
        minute.add(":00");
        minute.add(":30");

        for (int i = 0; i < ROW_SIZE; i++) {
            ColTitle colTitle = new ColTitle();
            String c = new Character((char) (i + 65)).toString();
            colTitle.setRoomNumber(c);
            if (i % 2 == 0) {
                String str = hour + minute.get(0) + "~";
                hour++;
                str += hour + minute.get(1);
                colTitle.setRoomTypeName(str);
            } else {
                String str = hour + minute.get(1) + "~";
                hour += 2;
                str += hour + minute.get(0);
                colTitle.setRoomTypeName(str);
            }
            colTitles.add(colTitle);
        }
        return colTitles;
    }

    private ArrayList<Cell> genCellData() {
        ArrayList<Cell> cellList = new ArrayList<>();
        for (int i = 0; i < ROW_SIZE * PAGE_SIZE; i++) {
            Cell cell = new Cell();
            Random random = new Random();
            int number = random.nextInt(15);
            if (number == 1 || number == 2 || number == 3 || number == 4 || number == 5) {
                cell.setStatus(number);
                cell.setPlaceName(PLACE_NAME.get(number));
                cell.setSubjectName(SUBJECT_NAME.get(number));
            } else {
                cell.setStatus(0);
            }
            cellList.add(cell);
        }
        return cellList;
    }























}


