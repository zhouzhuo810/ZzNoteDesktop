package me.zhouzhuo810.zznote;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import me.zhouzhuo810.zznote.api.Api;
import me.zhouzhuo810.zznote.api.entity.GetNotesEntity;
import me.zhouzhuo810.zznote.api.entity.HeartBeatEntity;
import me.zhouzhuo810.zznote.api.entity.ModifyNoteContentEntity;
import me.zhouzhuo810.zznote.event.ExitEvent;
import me.zhouzhuo810.zznote.event.OpenNoteEvent;
import me.zhouzhuo810.zznote.event.SelectionChangeEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

public class Controller implements Initializable {

    @FXML
    public TextArea tvContent;
    @FXML
    public VBox ipLayout;
    @FXML
    public TextField ipView;
    @FXML
    public Label openNoteHint;
    private long noteId;
    private Disposable subscribe;
    private Disposable subscribe1;

    private boolean hasRetryOnce = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        EventBus.getDefault().register(this);

        tvContent.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateNote(newValue);
            }
        });

        tvContent.selectionProperty().addListener(new ChangeListener<IndexRange>() {
            @Override
            public void changed(ObservableValue<? extends IndexRange> observable, IndexRange oldValue, IndexRange newValue) {
                int start = newValue.getStart();
                int end = newValue.getEnd();
                updateSelection(start, end);
            }
        });

        ipView.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tempToConnect(newValue);
            }
        });

        Preferences prefs = Preferences.userNodeForPackage(Controller.class);
        String lastIp = prefs.get("lastIp", null);
        if (lastIp != null) {
            ipView.setText(lastIp);
        }


        getContent();

        subscribe = Observable.interval(1, TimeUnit.SECONDS)
                .compose(RxHelper.io_main())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        heartBeat();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });


    }

    private void tempToConnect(String newValue) {
        String ip = newValue.trim().replace("。", ".");
        if (ip.matches("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}")) {
            Api.updateApi(ip);
            getContent();
        }
    }

    private void updateSelection(int start, int end) {
        if (Api.SERVER_IP0 == null) {
            return;
        }
        if (noteId == 0 || start == 0) {
            return;
        }
        subscribe1 = Api.getApi0()
                .updateSelection(noteId, start, end)
                .compose(RxHelper.io_main())
                .subscribe(new Consumer<ModifyNoteContentEntity>() {
                    @Override
                    public void accept(ModifyNoteContentEntity modifyNoteContentEntity) throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    private void updateNote(String newValue) {
        if (Api.SERVER_IP0 == null) {
            return;
        }
        if (noteId == 0) {
            return;
        }
        subscribe1 = Api.getApi0()
                .modifyNoteContent(noteId, newValue)
                .compose(RxHelper.io_main())
                .subscribe(new Consumer<ModifyNoteContentEntity>() {
                    @Override
                    public void accept(ModifyNoteContentEntity modifyNoteContentEntity) throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    private void getContent() {
        if (Api.SERVER_IP0 == null) {
            return;
        }
        Disposable subscribe = Api.getApi0()
                .getOpenedNote()
                .compose(RxHelper.io_main())
                .subscribe(new Consumer<GetNotesEntity>() {
                    @Override
                    public void accept(GetNotesEntity getNotesEntity) throws Exception {
                        if (getNotesEntity.getCode() == 1) {
                            GetNotesEntity.DataEntity data = getNotesEntity.getData();
                            String content = data.getContent();
                            noteId = data.getNoteId();
                            tvContent.setText(content);
                            showNoOpen(false, getNotesEntity.getMsg());
                        } else {
                            showNoOpen(true, getNotesEntity.getMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showMsg("错误", throwable.getMessage());
                    }
                });
    }

    private void showMsg(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.titleProperty().set(title);
        alert.headerTextProperty().set(msg);
        alert.showAndWait();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOpenNoteEvent(OpenNoteEvent event) {
        noteId = event.getNoteId();
        heartBeat();
    }

    /**
     * 网络请求获取便签内容
     */
    private void heartBeat() {
        if (Api.SERVER_IP0 == null) {
            return;
        }
        Disposable subscribe = Api.getApi0()
                .heartBeat()
                .compose(RxHelper.io_main())
                .subscribe(new Consumer<HeartBeatEntity>() {
                    @Override
                    public void accept(HeartBeatEntity heartBeatEntity) throws Exception {
                        boolean noteChange = noteId != heartBeatEntity.getEditingNoteId();
                        noteId = heartBeatEntity.getEditingNoteId();
                        System.out.println("noteId=" + noteId);
                        hideIpInput();
                        hasRetryOnce = false;
                        if (noteChange) {
                            getContent();
                        } else if (noteId == 0) {
                            showNoOpen(true, "请打开一篇便签");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showIpInput();
                        if (!hasRetryOnce) {
                            hasRetryOnce = true;
                            getContent();
                        }
                    }
                });
    }

    private void hideIpInput() {
        ipLayout.setVisible(false);
    }

    private void showIpInput() {
        ipLayout.setVisible(true);
    }

    private void showNoOpen(boolean visible, String msg) {
        openNoteHint.setVisible(visible);
        tvContent.setVisible(!visible);
        if (msg != null) {
            openNoteHint.setText(msg);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectionChangeEvent(SelectionChangeEvent event) {
        System.out.println(event.getStart());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onExitEvent(ExitEvent event) {
        cancelDisposable(subscribe1);
        cancelDisposable(subscribe);
        EventBus.getDefault().unregister(this);
    }

    private void cancelDisposable(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
