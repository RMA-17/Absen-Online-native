<?php

include 'connection.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    $username = $_POST['username'];
    $absen_type = $_POST['tipe_absen'];
    $longitude = $_POST['longitude'];
    $latitude = $_POST['latitude'];
    $keterangan = $_POST['keterangan'];
    $jam_masuk = $_POST['jam_masuk'];
    $tanggal = $_POST['tanggal_sekarang'];
    $isTelat = $_POST['isTelat'];

    $queryCheckData = "SELECT COUNT(*) 'total' FROM v_tbl_karyawan WHERE username = '$username'";
    $exec_queryCheckData = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryCheckData));

    if ($exec_queryCheckData['total'] == 0) {

        $response['message'] = "User tidak ditemukan, pastikan password dan penamaan username sudah benar, lalu coba lagi!";
        $response['code'] = 404;
        $response['status'] = false;

        echo json_encode($response);
    } else {
        $queryGetData = "SELECT id_karyawan FROM v_tbl_karyawan WHERE username = '$username'";
        $exec_queryGetData = mysqli_fetch_array(mysqli_query($_AUTH, $queryGetData));

        $id_karyawan = $exec_queryGetData['id_karyawan'];
        $type = $absen_type;
        $long = $longitude;
        $lat = $latitude;
        $ket = $keterangan;
        $photo = $_FILES['photo_absen']['name'];

        if ($type == '1') {
            $queryGetAwal = "SELECT nilai FROM tbl_pengaturan_absen WHERE id = '2'";
            $exec_queryGetSettings = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryGetAwal));

            $queryGetAkhir = "SELECT nilai FROM tbl_pengaturan_absen WHERE id = '3'";
            $exec_queryGetSettingsAkhir = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryGetAkhir));

            $queryGetBoolSiang = "SELECT nilai FROM tbl_pengaturan_absen WHERE id = '1'";
            $exec_queryGetSettingsBoolSiang = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryGetBoolSiang));

            $nilaiAwal = array();
            $nilaiAkhir = array();
            $absen_siang_diperlukan = array();

            for ($i = 0; $i < count($exec_queryGetSettings); $i++) {
                array_push($nilaiAwal, $exec_queryGetSettings['nilai']);
            }

            for ($i = 0; $i < count($exec_queryGetSettingsAkhir); $i++) {
                array_push($nilaiAkhir, $exec_queryGetSettingsAkhir['nilai']);
            }

            for ($i = 0; $i < count($exec_queryGetSettingsBoolSiang); $i++) {
                array_push($absen_siang_diperlukan, $exec_queryGetSettingsBoolSiang['nilai']);
            }

            $waktu_absen_awal = $nilaiAwal[0];
            $waktu_absen_akhir = $nilaiAkhir[0];
            $perlu_absen_siang = $absen_siang_diperlukan[0];

            $inputAbsen = mysqli_query($_AUTH, "INSERT INTO tbl_absensi (id_karyawan, jam_masuk_pagi, jam_awal_pagi, jam_akhir_pagi, longitude_pagi, latitude_pagi, photo_pagi, hadir, telat, absen_siang_diperlukan) VALUES ('$id_karyawan', '$jam_masuk', '$waktu_absen_awal', '$waktu_absen_akhir', '$long', '$lat', '$photo', '1', '$isTelat', '$perlu_absen_siang')");

            if (file_exists('images/' . $_FILES['photo_absen']['name'])) {
                chmod($_FILES['photo_absen']['name'], 0755); //Change the file permissions if allowed
                unlink($_FILES['photo_absen']['name']); //remove the file
            }

            if ($inputAbsen && move_uploaded_file($_FILES['photo_absen']['tmp_name'], 'images/' . $photo)) {

                $response['message'] = "Sukses menginput absensi!";
                $response['code'] = 200;
                $response['status'] = true;
                $response['tipe_absen'] = "Absen Pagi";

                echo json_encode($response);
            } else {

                $response['message'] = "Gagal menginput absensi, silahkan coba lagi!";
                $response['code'] = 400;
                $response['status'] = false;

                echo json_encode($response);
            }
        } else if ($absen_type == '2') {

            $queryGetAwal = "SELECT nilai FROM tbl_pengaturan_absen WHERE id = '4'";
            $exec_queryGetSettings = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryGetAwal));

            $queryGetAkhir = "SELECT nilai FROM tbl_pengaturan_absen WHERE id = '5'";
            $exec_queryGetSettingsAkhir = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryGetAkhir));

            $queryGetBoolSiang = "SELECT nilai FROM tbl_pengaturan_absen WHERE id = '1'";
            $exec_queryGetSettingsBoolSiang = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryGetBoolSiang));

            $nilaiAwal = array();
            $nilaiAkhir = array();
            $absen_siang_diperlukan = array();

            for ($i = 0; $i < count($exec_queryGetSettings); $i++) {
                array_push($nilaiAwal, $exec_queryGetSettings['nilai']);
            }

            for ($i = 0; $i < count($exec_queryGetSettingsAkhir); $i++) {
                array_push($nilaiAkhir, $exec_queryGetSettingsAkhir['nilai']);
            }

            for ($i = 0; $i < count($exec_queryGetSettingsBoolSiang); $i++) {
                array_push($absen_siang_diperlukan, $exec_queryGetSettingsBoolSiang['nilai']);
            }

            $waktu_absen_awal = $nilaiAwal[0];
            $waktu_absen_akhir = $nilaiAkhir[0];
            $perlu_absen_siang = $absen_siang_diperlukan[0];

            $inputAbsen = mysqli_query($_AUTH, "UPDATE tbl_absensi SET jam_masuk_siang = '$jam_masuk', jam_awal_siang = '$waktu_absen_awal', jam_akhir_siang = '$waktu_absen_akhir', longitude_siang = '$long', latitude_siang = '$lat', photo_siang = '$photo' WHERE tanggal >= '$tanggal' AND id_karyawan = $id_karyawan");

            if (file_exists('images/' . $_FILES['photo_absen']['name'])) {
                chmod($_FILES['photo_absen']['name'], 0755); //Change the file permissions if allowed
                unlink($_FILES['photo_absen']['name']); //remove the file
            }

            if ($inputAbsen && move_uploaded_file($_FILES['photo_absen']['tmp_name'], 'images/' . $photo)) {

                $response['message'] = "Sukses menginput absensi!";
                $response['code'] = 200;
                $response['status'] = true;
                $response['tipe_absen'] = "Absen Siang";

                echo json_encode($response);
            } else {

                $response['message'] = "Gagal menginput absensi, silahkan coba lagi!";
                $response['code'] = 400;
                $response['status'] = false;

                echo json_encode($response);
            }
        } else if ($absen_type ==  '3') {
            $queryGetAwal = "SELECT nilai FROM tbl_pengaturan_absen WHERE id = '6'";
            $exec_queryGetSettings = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryGetAwal));

            $queryGetAkhir = "SELECT nilai FROM tbl_pengaturan_absen WHERE id = '7'";
            $exec_queryGetSettingsAkhir = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryGetAkhir));

            $queryGetBoolSiang = "SELECT nilai FROM tbl_pengaturan_absen WHERE id = '1'";
            $exec_queryGetSettingsBoolSiang = mysqli_fetch_assoc(mysqli_query($_AUTH, $queryGetBoolSiang));

            $nilaiAwal = array();
            $nilaiAkhir = array();
            $absen_siang_diperlukan = array();

            for ($i = 0; $i < count($exec_queryGetSettings); $i++) {
                array_push($nilaiAwal, $exec_queryGetSettings['nilai']);
            }

            for ($i = 0; $i < count($exec_queryGetSettingsAkhir); $i++) {
                array_push($nilaiAkhir, $exec_queryGetSettingsAkhir['nilai']);
            }

            for ($i = 0; $i < count($exec_queryGetSettingsBoolSiang); $i++) {
                array_push($absen_siang_diperlukan, $exec_queryGetSettingsBoolSiang['nilai']);
            }

            $waktu_absen_awal = $nilaiAwal[0];
            $waktu_absen_akhir = $nilaiAkhir[0];
            $perlu_absen_siang = $absen_siang_diperlukan[0];

            $inputAbsen = mysqli_query($_AUTH, "UPDATE tbl_absensi SET jam_masuk_pulang = '$jam_masuk', jam_awal_pulang = '$waktu_absen_awal', absen_siang_diperlukan = '$perlu_absen_siang', jam_akhir_pulang = '$waktu_absen_awal', longitude_pulang = '$long', latitude_pulang = '$lat', photo_pulang = '$photo', full_absen = '1' WHERE tanggal >= '$tanggal' AND id_karyawan = $id_karyawan");

            if (file_exists('images/' . $_FILES['photo_absen']['name'])) {
                chmod($_FILES['photo_absen']['name'], 0755); //Change the file permissions if allowed
                unlink($_FILES['photo_absen']['name']); //remove the file
            }

            if ($inputAbsen && move_uploaded_file($_FILES['photo_absen']['tmp_name'], 'images/' . $photo)) {

                $response['message'] = "Sukses menginput absensi!";
                $response['code'] = 200;
                $response['status'] = true;
                $response['tipe_absen'] = "Absen Pulang";

                echo json_encode($response);
            } else {

                $response['message'] = "Gagal menginput absensi, silahkan coba lagi!";
                $response['code'] = 400;
                $response['status'] = false;

                echo json_encode($response);
            }
        } else if ($absen_type == '4' || '5') {
            $tipe_absen = "";

            $query_check_if_exist = mysqli_query($_AUTH, "SELECT EXISTS(SELECT * from tbl_absensi WHERE tanggal >= '$tanggal' AND id_karyawan = '$id_karyawan') as RESULT;");
            $exec_checkIfExist = mysqli_fetch_assoc($query_check_if_exist);

            if ($exec_checkIfExist['RESULT'] == 0) {
                if ($absen_type == '4') {
                    $tipe_absen = "Cuti";
                    $inputAbsen = mysqli_query($_AUTH, "INSERT INTO tbl_absensi (id_karyawan, cuti, izin, keterangan, longitude_izin_cuti, latitude_izin_cuti) VALUES ('$id_karyawan','1', '0', '$ket', '$long', '$lat')");
                    if ($inputAbsen) {

                        $response['message'] = "Sukses menginput absensi!";
                        $response['code'] = 200;
                        $response['status'] = true;
                        $response['tipe_absen'] = $tipe_absen;

                        echo json_encode($response);
                    } else {
                        $response['message'] = "Gagal menginput absensi, silahkan coba lagi!";
                        $response['code'] = 400;
                        $response['status'] = false;

                        echo json_encode($response);
                    }
                } else if ($absen_type == '5') {
                    $tipe_absen = "Izin";
                    $inputAbsen = mysqli_query($_AUTH, "INSERT INTO tbl_absensi (id_karyawan, cuti, izin, keterangan, longitude_izin_cuti, latitude_izin_cuti) VALUES ('$id_karyawan','0', '1', '$ket', '$long', '$lat')");
                    if ($inputAbsen) {

                        $response['message'] = "Sukses menginput absensi!";
                        $response['code'] = 200;
                        $response['status'] = true;
                        $response['tipe_absen'] = $tipe_absen;

                        echo json_encode($response);
                    } else {

                        $response['message'] = "Gagal menginput absensi, silahkan coba lagi!";
                        $response['code'] = 400;
                        $response['status'] = false;

                        echo json_encode($response);
                    }
                }
            } else if ($exec_checkIfExist['RESULT'] == 1) {
                if ($absen_type == '4') {
                    $tipe_absen = "Cuti";
                    $inputAbsen = mysqli_query($_AUTH, "UPDATE tbl_absensi SET cuti = '1', izin = '0', keterangan = '$ket', longitude_izin_cuti = '$long', latitude_izin_cuti = '$lat' WHERE tanggal >= '$tanggal' AND id_karyawan = $id_karyawan");
                    if ($inputAbsen) {

                        $response['message'] = "Sukses menginput absensi!";
                        $response['code'] = 200;
                        $response['status'] = true;
                        $response['tipe_absen'] = $tipe_absen;

                        echo json_encode($response);
                    } else {

                        $response['message'] = "Gagal menginput absensi, silahkan coba lagi!";
                        $response['code'] = 400;
                        $response['status'] = false;

                        echo json_encode($response);
                    }
                } else if ($absen_type == '5') {
                    $tipe_absen = "Izin";
                    $inputAbsen = mysqli_query($_AUTH, "UPDATE tbl_absensi SET cuti = '0', izin = '1', keterangan = '$ket', longitude_izin_cuti = '$long', latitude_izin_cuti = '$lat', WHERE tanggal >= '$tanggal' AND id_karyawan = $id_karyawan");
                    if ($inputAbsen) {

                        $response['message'] = "Sukses menginput absensi!";
                        $response['code'] = 200;
                        $response['status'] = true;
                        $response['tipe_absen'] = $tipe_absen;

                        echo json_encode($response);
                    } else {

                        $response['message'] = "Gagal menginput absensi, silahkan coba lagi!";
                        $response['code'] = 400;
                        $response['status'] = false;

                        echo json_encode($response);
                    }
                }
            }
        }
    }
} else {

    $response['message'] = "Akses ditolak, API ini menggunakan metode POST";
    $response['code'] = 400;
    $response['status'] = false;

    echo json_encode($response);
}
//Code by Raka
