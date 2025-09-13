import {useRef, useState} from 'react';
import {Button, Grid, GridColumn, Notification} from '@vaadin/react-components';
import {processFile} from 'Frontend/generated/EmployeesCommonProjectService';

export default function EmployeeCollaborationView() {
    const [file, setFile] = useState<File | null>(null);
    const [results, setResults] = useState<any[]>([]);
    const [loading, setLoading] = useState(false);
    const fileInputRef = useRef<HTMLInputElement>(null);

    const handleChooseFile = () => {
        fileInputRef.current?.click();
    };

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const files = event.target.files;
        if (files && files.length > 0) {
            setFile(files[0]);
        } else {
            setFile(null);
        }
    };

    const handleProcess = async () => {
        if (!file) return;
        setLoading(true);
        try {
            const arrayBuffer = await file.arrayBuffer();
            const uint8Array = new Uint8Array(arrayBuffer);
            const data = await processFile(Array.from(uint8Array));
            setResults(data || []);
            Notification.show('File processed successfully', {
                duration: 3000,
                position: 'bottom-end',
                theme: 'success'
            });
        } catch (error) {
            Notification.show('Error processing file', {duration: 3000, position: 'bottom-end', theme: 'error'});
        } finally {
            setLoading(false);
        }
    };

    return (
        <main className="w-full h-full flex flex-col box-border gap-s p-m">
            <h1 className="text-xl m-0 font-light">Employees Common Projects</h1>
            <div className="flex flex-col gap-s items-start">
                <div className="flex gap-s items-center">
                    <input
                        type="file"
                        accept=".csv"
                        style={{display: 'none'}}
                        ref={fileInputRef}
                        onChange={handleFileChange}
                    />
                    <Button onClick={handleChooseFile} theme="secondary">
                        Choose File
                    </Button>
                    <span>{file ? file.name : 'No file selected'}</span>
                </div>
                <Button onClick={handleProcess} disabled={!file || loading} theme="primary">
                    Process File
                </Button>
            </div>
            <Grid items={results} className="mt-m">
                <GridColumn path="emp1Id" header="Employee ID #1"/>
                <GridColumn path="emp2Id" header="Employee ID #2"/>
                <GridColumn path="projectId" header="Project ID"/>
                <GridColumn path="daysWorkedTogether" header="Days worked"/>
            </Grid>
        </main>
    );
}
